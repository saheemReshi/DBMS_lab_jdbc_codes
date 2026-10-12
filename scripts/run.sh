#!/usr/bin/env bash
set -euo pipefail

repo_dir="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")/.." && pwd)"

case "${1:-}" in
  basics) example=jdbc-basics; main_class=Demo ;;
  transactions) example=company-transactions; main_class=App ;;
  report) example=company-transactions; main_class=Report ;;
  stock) example=stock-management; main_class=StockManagementApp ;;
  *) echo "Usage: bash scripts/run.sh {basics|transactions|report|stock}" >&2; exit 2 ;;
esac

if [[ -z "${DB_USER:-}" || "${DB_PASSWORD+x}" != x ]]; then
  echo "Set DB_USER and DB_PASSWORD before running an example." >&2
  exit 1
fi
if [[ -z "${JDBC_JAR:-}" || ! -f "$JDBC_JAR" ]]; then
  echo "Set JDBC_JAR to the path of your MySQL Connector/J JAR." >&2
  exit 1
fi

# Each project has its own output directory because the examples use default Java packages.
output_dir="$repo_dir/out/$example"
mkdir -p "$output_dir"
javac -d "$output_dir" "$repo_dir/examples/$example/src/"*.java
java -cp "$output_dir:$JDBC_JAR" "$main_class"
