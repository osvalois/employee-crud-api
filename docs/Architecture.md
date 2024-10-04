%%{init: {'theme': 'base', 'themeVariables': { 'primaryColor': '#007bff', 'secondaryColor': '#f8f9fa', 'tertiaryColor': '#6c757d'}}}%%
flowchart TD
    subgraph Cliente ["Cliente"]
        C_Start([Inicio]) --> C_Upload[Cargar archivo Excel a S3]
        C_Upload --> C_Wait{Esperar procesamiento}
        C_Wait --> C_Notify[Recibir notificación]
        C_Notify --> C_End([Fin])
    end

    subgraph AWS ["Sistema AWS"]
        subgraph S3 ["Amazon S3"]
            S3_Receive[Recibir archivo]
        end

        subgraph Lambda ["AWS Lambda"]
            L_Trigger[Trigger de procesamiento]
            L_Validate{Validar formato}
        end

        subgraph Batch ["AWS Batch"]
            B_Init[Inicializar job]
            B_Process[["Procesar datos<br>(subproceso)"]]
        end

        subgraph DynamoDB ["Amazon DynamoDB"]
            D_Store[Almacenar resultados]
        end

        subgraph CloudWatch ["Amazon CloudWatch"]
            CW_Monitor[Monitorear proceso]
            CW_Alert{Detectar anomalías}
        end

        subgraph SNS ["Amazon SNS"]
            SNS_Notify[Notificar resultado]
        end
    end

    C_Upload -.-> S3_Receive
    S3_Receive --> L_Trigger
    L_Trigger --> L_Validate
    L_Validate -->|Válido| B_Init
    L_Validate -->|Inválido| SNS_Notify
    B_Init --> B_Process
    B_Process -->|Chunk procesado| D_Store
    D_Store -.-> CW_Monitor
    B_Process -.-> CW_Monitor
    CW_Monitor --> CW_Alert
    CW_Alert -->|Anomalía detectada| SNS_Notify
    B_Process -->|Procesamiento completo| SNS_Notify
    SNS_Notify -.-> C_Notify

    subgraph B_Process [Subproceso: Procesar datos]
        direction TB
        P_Start([Inicio]) --> P_Read[Leer chunk de datos]
        P_Read --> P_Transform[Transformar datos]
        P_Transform --> P_Validate{Validar datos}
        P_Validate -->|Válidos| P_Store[Almacenar en DynamoDB]
        P_Validate -->|Inválidos| P_Log[Registrar error]
        P_Store --> P_Check{Más chunks?}
        P_Log --> P_Check
        P_Check -->|Sí| P_Read
        P_Check -->|No| P_Report[Generar reporte]
        P_Report --> P_End([Fin])
    end

    %% Anotaciones
    L_Validate -->|"Usa regex y<br>validación de esquema"| AN_Validate[/"Validación detallada"/]
    B_Process -->|"Procesamiento<br>distribuido"| AN_Process[/"Usa EC2 Spot Instances<br>para optimizar costos"/]
    CW_Monitor -->|"Métricas personalizadas"| AN_Monitor[/"Monitoreo en tiempo real<br>de rendimiento y errores"/]

    %% Estilos
    classDef clientColor fill:#e6f7ff,stroke:#91d5ff,stroke-width:2px;
    classDef awsColor fill:#fff1f0,stroke:#ffa39e,stroke-width:2px;
    classDef processColor fill:#f6ffed,stroke:#b7eb8f,stroke-width:2px;
    classDef decisionColor fill:#fff7e6,stroke:#ffd591,stroke-width:2px;
    classDef annotationColor fill:#f0f5ff,stroke:#adc6ff,stroke-width:1px,stroke-dasharray: 5 5;

    class C_Start,C_Upload,C_Wait,C_Notify,C_End clientColor;
    class S3_Receive,L_Trigger,B_Init,D_Store,CW_Monitor,SNS_Notify awsColor;
    class P_Start,P_Read,P_Transform,P_Store,P_Log,P_Report,P_End processColor;
    class L_Validate,P_Validate,P_Check,CW_Alert decisionColor;
    class AN_Validate,AN_Process,AN_Monitor annotationColor;