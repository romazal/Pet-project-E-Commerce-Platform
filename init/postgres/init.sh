#!/bin/bash

# Create the databases if they don't exist

set -e

if ! psql -U $POSTGRES_USER -lqt | cut -d \| -f 1 | grep -qw order; then
    createdb -U $POSTGRES_USER order
fi

if ! psql -U $POSTGRES_USER -lqt | cut -d \| -f 1 | grep -qw payment; then
    createdb -U $POSTGRES_USER payment
fi

if ! psql -U $POSTGRES_USER -lqt | cut -d \| -f 1 | grep -qw transaction_history; then
    createdb -U $POSTGRES_USER transaction_history
fi

if ! psql -U $POSTGRES_USER -lqt | cut -d \| -f 1 | grep -qw shipping; then
    createdb -U $POSTGRES_USER shipping
fi

if ! psql -U $POSTGRES_USER -lqt | cut -d \| -f 1 | grep -qw product; then
    createdb -U $POSTGRES_USER product
fi
