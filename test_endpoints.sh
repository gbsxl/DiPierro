#!/bin/bash

# Configuration
BASE_URL="http://localhost:8080"

echo "======================================"
echo "    Testing DiPierro Endpoints        "
echo "======================================"

# --- 1. Create Actors ---
echo -e "\n[1] Creating Actors..."
curl -s -X POST "$BASE_URL/actor" \
  -H "Content-Type: application/json" \
  -d '{"address": "Rua das Flores, 123"}' > /dev/null

curl -s -X POST "$BASE_URL/actor" \
  -H "Content-Type: application/json" \
  -d '{"address": "Avenida Paulista, 1000"}' > /dev/null

echo "Fetching created Actors..."
ACTORS=$(curl -s "$BASE_URL/actor")
ACTOR1_ID=$(echo "$ACTORS" | jq -r '.[0].id')
ACTOR2_ID=$(echo "$ACTORS" | jq -r '.[1].id')

if [ -z "$ACTOR1_ID" ] || [ "$ACTOR1_ID" == "null" ]; then
    echo "Failed to fetch Actor 1 ID. Make sure the application is running on port 8080."
    exit 1
fi

echo " -> Actor 1 ID: $ACTOR1_ID"
echo " -> Actor 2 ID: $ACTOR2_ID"

# --- 2. Create Person ---
echo -e "\n[2] Creating Person linked to Actor 1..."
curl -s -X POST "$BASE_URL/person" \
  -H "Content-Type: application/json" \
  -d "{
    \"completeName\": \"João Silva\",
    \"cpf\": \"12345678901\",
    \"gender\": \"MALE\",
    \"phoneNumber\": \"11999999999\",
    \"email\": \"joao@email.com\",
    \"actorId\": \"$ACTOR1_ID\"
}" > /dev/null
echo " -> Person creation requested."

# --- 3. Create Business ---
echo -e "\n[3] Creating Business linked to Actor 2..."
curl -s -X POST "$BASE_URL/business" \
  -H "Content-Type: application/json" \
  -d "{
    \"legalName\": \"Empresa Tech SA\",
    \"cnpj\": \"12345678901234\",
    \"fantasyName\": \"Tech Solutions\",
    \"phoneNumber\": \"11888888888\",
    \"email\": \"contato@tech.com\",
    \"isPublicCompany\": false,
    \"actorId\": \"$ACTOR2_ID\"
}" > /dev/null
echo " -> Business creation requested."

# --- 4. Create Association ---
echo -e "\n[4] Creating Association between Actor 1 and Actor 2..."
curl -s -X POST "$BASE_URL/association" \
  -H "Content-Type: application/json" \
  -d "{
    \"associationType\": \"SOCIO\",
    \"source\": \"Receita Federal\",
    \"confidenceLevel\": 9,
    \"associationEnded\": false,
    \"associationStart\": \"2023-01-01T00:00:00Z\",
    \"firstActorId\": \"$ACTOR1_ID\",
    \"secondActorId\": \"$ACTOR2_ID\"
}" > /dev/null
echo " -> Association creation requested."

# --- 5. Create Transaction ---
echo -e "\n[5] Creating Transaction from Actor 1 to Actor 2..."
curl -s -X POST "$BASE_URL/transaction" \
  -H "Content-Type: application/json" \
  -d "{
    \"value\": 1500.50,
    \"currency\": \"BRL\",
    \"transactionDate\": \"2023-12-01T12:00:00Z\",
    \"actorSenderId\": \"$ACTOR1_ID\",
    \"actorReceiverId\": \"$ACTOR2_ID\"
}" > /dev/null
echo " -> Transaction creation requested."

# --- Verification ---
echo -e "\n\n======================================"
echo "    Checking Created Objects          "
echo "======================================"

echo -e "\n-> Persons (GET /person):"
curl -s "$BASE_URL/person" | jq

echo -e "\n-> Businesses (GET /business):"
curl -s "$BASE_URL/business" | jq

echo -e "\n-> Associations (GET /association):"
curl -s "$BASE_URL/association" | jq

echo -e "\n-> Transactions (GET /transaction):"
curl -s "$BASE_URL/transaction" | jq

echo -e "\nDone! If everything returned properly, your mappings are correct! :)"
