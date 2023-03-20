#!/usr/bin/env bash

simulate() {
    python3.11 db_gen.py 1>/dev/null
}

populate() {
    export PGHOST="csce-315-db.engr.tamu.edu"
    export PGDATABASE="csce315331_delta"
    export PGUSER="csce315331_delta_master"
    export PGPASSWORD="airplane"
    psql -f populate.sql
    unset PGHOST
    unset PGDATABASE
    unset PGUSER
    unset PQPASSWORD
}

echo "Running simulation"
simulate

echo "Populating Database"
populate