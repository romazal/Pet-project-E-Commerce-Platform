#!/bin/bash

# Create the databases if they don't exist

set -e

if ! psql -U $POSTGRES_USER -lqt | cut -d \| -f 1 | grep -qw order; then
    createdb -U $POSTGRES_USER order
fi

if ! psql -U $POSTGRES_USER -lqt | cut -d \| -f 1 | grep -qw payment; then
    createdb -U $POSTGRES_USER payment
fi

if ! psql -U $POSTGRES_USER -lqt | cut -d \| -f 1 | grep -qw product_movement; then
    createdb -U $POSTGRES_USER product_movement
fi

if ! psql -U $POSTGRES_USER -lqt | cut -d \| -f 1 | grep -qw shipment; then
    createdb -U $POSTGRES_USER shipment
fi

if ! psql -U $POSTGRES_USER -lqt | cut -d \| -f 1 | grep -qw product; then
    createdb -U $POSTGRES_USER product
fi
