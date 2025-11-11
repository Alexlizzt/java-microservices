#!/bin/bash
set -e

ROOT_DIR=$(pwd)
PID_FILE="$ROOT_DIR/services.pid"

stop_services() {
    if [[ -f "$PID_FILE" ]]; then
        echo "Deteniendo servicios..."
        while read -r pid; do
            if kill "$pid" 2>/dev/null; then
                echo "  → Proceso $pid detenido"
            fi
        done < "$PID_FILE"
        rm -f "$PID_FILE"
        echo "✅ Todos los servicios detenidos."
    else
        echo "⚠️  No hay servicios en ejecución."
    fi
    exit 0
}

# Si se pasa el argumento "-stop", se detienen los servicios
if [[ "$1" == "-stop" ]]; then
    stop_services
fi

# Función para abrir un servicio en una nueva terminal y ejecutar mvn spring-boot:run
run_service() {
    local service_dir="$1"
    local service_name="$2"
    local port="$3"

    echo "🚀 Iniciando $service_name en una nueva terminal (puerto $port)..."
    
    # Abrir nueva terminal y ejecutar mvn spring-boot:run
    gnome-terminal --title="$service_name" -- bash -c "
        cd \"$service_dir\" || exit 1
        mvn spring-boot:run -Dspring-boot.run.arguments=\"--server.port=$port\" &
        echo \$! >> \"$PID_FILE\"
        wait
    "

    # Esperar a que el puerto esté disponible
    echo -n "⏳ Esperando a que $service_name esté listo en el puerto $port "
    until nc -z localhost "$port"; do
        printf "."
        sleep 1
    done
    echo " ✅ listo!"
}

# 🧱 Iniciar servicios en orden
run_service "$ROOT_DIR/config-server" "config-server" 8888
run_service "$ROOT_DIR/discovery-service" "discovery-service" 8761
run_service "$ROOT_DIR/product-service" "product-service" 8081 &
run_service "$ROOT_DIR/booking-service" "booking-service" 8082 &
wait
run_service "$ROOT_DIR/api-gateway" "api-gateway" 8080

echo
echo "✅ Todos los servicios están ejecutándose en terminales separadas."
echo "👉 Para detenerlos, usa: ./start-services.sh stop"