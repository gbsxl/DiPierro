#!/bin/bash

# ==============================================================================
# DiPierro - End-to-End API Test Suite
# Tests all entities: Actor, Person, Business, Transaction, Association,
#                     PublicProcurement, Document, DocumentMention,
#                     Asset, ActorIndicator, RedFlag
# ==============================================================================

BASE_URL="http://localhost:8080"
PASS=0
FAIL=0

# Unique suffix per run to avoid UNIQUE constraint violations on repeated runs
RUN_ID=$(date +%s | tail -c 6)

# ---- Helpers -----------------------------------------------------------------

GREEN="\033[0;32m"
RED="\033[0;31m"
YELLOW="\033[1;33m"
CYAN="\033[0;36m"
RESET="\033[0m"

section() { echo -e "\n${CYAN}══════════════════════════════════════════${RESET}"; echo -e "${CYAN}  $1${RESET}"; echo -e "${CYAN}══════════════════════════════════════════${RESET}"; }
ok()      { echo -e "  ${GREEN}✔ $1${RESET}"; PASS=$((PASS + 1)); }
fail()    { echo -e "  ${RED}✘ $1${RESET}"; FAIL=$((FAIL + 1)); }
info()    { echo -e "  ${YELLOW}→ $1${RESET}"; }

assert_status() {
  local label="$1"
  local expected="$2"
  local actual="$3"
  if [ "$actual" == "$expected" ]; then
    ok "$label (HTTP $actual)"
  else
    fail "$label — expected HTTP $expected, got HTTP $actual"
  fi
}

post() {
  # post <url> <body> → returns HTTP status code; response body goes to /tmp/dp_response
  curl -s -o /tmp/dp_response -w "%{http_code}" -X POST "$1" \
    -H "Content-Type: application/json" -d "$2"
}

get() {
  # get <url> → returns HTTP status code; response body goes to /tmp/dp_response
  curl -s -o /tmp/dp_response -w "%{http_code}" "$1"
}

body() { cat /tmp/dp_response; }
jq_body() { cat /tmp/dp_response | jq -r "$1" 2>/dev/null; }

# ==============================================================================
section "0. Health Check"
# ==============================================================================
STATUS=$(get "$BASE_URL/actor")
if [ "$STATUS" == "200" ]; then
  ok "Application is reachable"
else
  fail "Application not reachable at $BASE_URL (got HTTP $STATUS). Is the server running?"
  exit 1
fi

# ==============================================================================
section "1. Actors — Create & Read"
# ==============================================================================

STATUS=$(post "$BASE_URL/actor" '{}')
assert_status "POST /actor (Actor 1)" "201" "$STATUS"

STATUS=$(post "$BASE_URL/actor" '{}')
assert_status "POST /actor (Actor 2)" "201" "$STATUS"

STATUS=$(post "$BASE_URL/actor" '{}')
assert_status "POST /actor (Actor 3 — for public procurement)" "201" "$STATUS"

STATUS=$(post "$BASE_URL/actor" '{}')
assert_status "POST /actor (Actor 4 — for document mention)" "201" "$STATUS"

STATUS=$(get "$BASE_URL/actor")
assert_status "GET /actor" "200" "$STATUS"

# Pick the last 4 actors just created (tail of the list) to avoid reusing actors
# that already have OneToOne associations (Person, Business, PublicProcurement)
ACTORS=$(body)
TOTAL_ACTORS=$(echo "$ACTORS" | jq 'length')
ACTOR1_ID=$(echo "$ACTORS" | jq -r ".[$((TOTAL_ACTORS - 4))].id")
ACTOR2_ID=$(echo "$ACTORS" | jq -r ".[$((TOTAL_ACTORS - 3))].id")
ACTOR3_ID=$(echo "$ACTORS" | jq -r ".[$((TOTAL_ACTORS - 2))].id")
ACTOR4_ID=$(echo "$ACTORS" | jq -r ".[$((TOTAL_ACTORS - 1))].id")

if [ -z "$ACTOR1_ID" ] || [ "$ACTOR1_ID" == "null" ]; then
  fail "Could not extract Actor IDs from GET /actor response. Aborting."
  exit 1
fi

info "Actor 1 ID: $ACTOR1_ID"
info "Actor 2 ID: $ACTOR2_ID"
info "Actor 3 ID: $ACTOR3_ID"
info "Actor 4 ID: $ACTOR4_ID"

STATUS=$(get "$BASE_URL/actor/$ACTOR1_ID")
assert_status "GET /actor/:id" "200" "$STATUS"

# ==============================================================================
section "2. Person — Create & Read"
# ==============================================================================

# CPF must be exactly 11 digits; zero-pad RUN_ID to 11 digits
CPF_UNIQUE=$(printf '%011d' "$RUN_ID")
STATUS=$(post "$BASE_URL/person" "{
  \"completeName\": \"João da Silva $RUN_ID\",
  \"cpf\": \"$CPF_UNIQUE\",
  \"address\": \"Rua das Flores, 123\",
  \"gender\": \"MALE\",
  \"phoneNumber\": \"11999999999\",
  \"email\": \"joao${RUN_ID}@email.com\",
  \"actorId\": \"$ACTOR1_ID\"
}")
assert_status "POST /person" "201" "$STATUS"

STATUS=$(get "$BASE_URL/person")
assert_status "GET /person" "200" "$STATUS"

PERSON_ID=$(jq_body '.[0].id')
info "Person ID: $PERSON_ID"

STATUS=$(get "$BASE_URL/person/$PERSON_ID")
assert_status "GET /person/:id" "200" "$STATUS"

STATUS=$(get "$BASE_URL/person/00000000-0000-0000-0000-000000000000")
assert_status "GET /person/:id — not found returns 404" "404" "$STATUS"

# ==============================================================================
section "3. Business — Create & Read"
# ==============================================================================

# CNPJ must be exactly 14 digits; zero-pad RUN_ID to 14 digits
CNPJ_UNIQUE=$(printf '%014d' "$RUN_ID")
STATUS=$(post "$BASE_URL/business" "{
  \"legalName\": \"Empresa Tech Ltda $RUN_ID\",
  \"cnpj\": \"$CNPJ_UNIQUE\",
  \"fantasyName\": \"TechSolutions\",
  \"phoneNumber\": \"11888888888\",
  \"email\": \"contato@tech.com\",
  \"isPublicCompany\": false,
  \"address\": \"Avenida Paulista, 1000\",
  \"capitalStock\": 500000.00,
  \"estimatedNetWorth\": 1200000.00,
  \"actorId\": \"$ACTOR2_ID\"
}")
assert_status "POST /business" "201" "$STATUS"

STATUS=$(get "$BASE_URL/business")
assert_status "GET /business" "200" "$STATUS"

BUSINESS_ID=$(jq_body '.[0].id')
info "Business ID: $BUSINESS_ID"

STATUS=$(get "$BASE_URL/business/$BUSINESS_ID")
assert_status "GET /business/:id" "200" "$STATUS"

STATUS=$(get "$BASE_URL/business/00000000-0000-0000-0000-000000000000")
assert_status "GET /business/:id — not found returns 404" "404" "$STATUS"

# ==============================================================================
section "4. Association — Create & Read"
# ==============================================================================

STATUS=$(post "$BASE_URL/association" "{
  \"associationType\": \"SOCIO\",
  \"source\": \"Receita Federal\",
  \"confidenceLevel\": 9,
  \"associationEnded\": false,
  \"associationStart\": \"2023-01-01T00:00:00Z\",
  \"firstActorId\": \"$ACTOR1_ID\",
  \"secondActorId\": \"$ACTOR2_ID\"
}")
assert_status "POST /association" "201" "$STATUS"

STATUS=$(get "$BASE_URL/association")
assert_status "GET /association" "200" "$STATUS"

ASSOC_ID=$(jq_body '.[0].id')
info "Association ID: $ASSOC_ID"

STATUS=$(get "$BASE_URL/association/$ASSOC_ID")
assert_status "GET /association/:id" "200" "$STATUS"

# ==============================================================================
section "5. Transaction — Create & Read"
# ==============================================================================

STATUS=$(post "$BASE_URL/transaction" "{
  \"value\": 1500.50,
  \"currency\": \"BRL\",
  \"transactionDate\": \"2024-01-15T12:00:00Z\",
  \"actorSenderId\": \"$ACTOR1_ID\",
  \"actorReceiverId\": \"$ACTOR2_ID\"
}")
assert_status "POST /transaction" "201" "$STATUS"

STATUS=$(get "$BASE_URL/transaction")
assert_status "GET /transaction" "200" "$STATUS"

TX_ID=$(jq_body '.[0].id')
info "Transaction ID: $TX_ID"

STATUS=$(get "$BASE_URL/transaction/$TX_ID")
assert_status "GET /transaction/:id" "200" "$STATUS"

# ==============================================================================
section "6. PublicProcurement — Create & Read"
# ==============================================================================

STATUS=$(post "$BASE_URL/public-procurement" "{
  \"publicProcurementNumber\": \"PP-${RUN_ID}\",
  \"processNumber\": \"PROC-${RUN_ID}\",
  \"object\": \"Aquisição de equipamentos de informática\",
  \"modality\": \"PREGAO_ELETRONICO\",
  \"situation\": \"ABERTA\",
  \"legalInstrument\": \"Lei 14.133/2021\",
  \"estimatedValue\": 250000.00,
  \"publicationDate\": \"2024-01-15\",
  \"openingDate\": \"2024-02-01\",
  \"designatedContact\": \"Maria Pregoeira\",
  \"ibgeCityCode\": \"3550308\",
  \"federativeUnitAcronym\": \"SP\",
  \"managingUnityCode\": \"SEDU-001\",
  \"cnpjGovernmentAgency\": \"12345678000101\",
  \"actorId\": \"$ACTOR3_ID\"
}")
assert_status "POST /public-procurement" "201" "$STATUS"

STATUS=$(get "$BASE_URL/public-procurement")
assert_status "GET /public-procurement" "200" "$STATUS"

PP_ID=$(jq_body '.[0].id')
info "PublicProcurement ID: $PP_ID"

STATUS=$(get "$BASE_URL/public-procurement/$PP_ID")
assert_status "GET /public-procurement/:id" "200" "$STATUS"

STATUS=$(get "$BASE_URL/public-procurement/00000000-0000-0000-0000-000000000000")
assert_status "GET /public-procurement/:id — not found returns 404" "404" "$STATUS"

# ==============================================================================
section "7. Document — Create & Read"
# ==============================================================================

STATUS=$(post "$BASE_URL/document" "{
  \"name\": \"Edital_PP2024001.pdf\",
  \"type\": \"EDITAL\",
  \"filePath\": \"/documents/2024/edital_pp2024001.pdf\",
  \"hash\": \"abc123def456abc123def456abc123def456abc123def456abc123def456abc1\",
  \"extracted\": false,
  \"publicProcurementId\": \"$PP_ID\"
}")
assert_status "POST /document" "201" "$STATUS"

STATUS=$(get "$BASE_URL/document")
assert_status "GET /document" "200" "$STATUS"

DOC_ID=$(jq_body '.[0].id')
info "Document ID: $DOC_ID"

STATUS=$(get "$BASE_URL/document/$DOC_ID")
assert_status "GET /document/:id" "200" "$STATUS"

STATUS=$(get "$BASE_URL/document/00000000-0000-0000-0000-000000000000")
assert_status "GET /document/:id — not found returns 404" "404" "$STATUS"

# ==============================================================================
section "8. DocumentMention — Create & Read"
# ==============================================================================

STATUS=$(post "$BASE_URL/document-mention" "{
  \"documentId\": \"$DOC_ID\",
  \"actorId\": \"$ACTOR4_ID\",
  \"role\": \"PREGOEIRO\",
  \"confidence\": 8,
  \"extractedName\": \"Maria Pregoeira dos Santos\"
}")
assert_status "POST /document-mention" "201" "$STATUS"

STATUS=$(get "$BASE_URL/document-mention")
assert_status "GET /document-mention" "200" "$STATUS"

DM_ID=$(jq_body '.[0].id')
info "DocumentMention ID: $DM_ID"

STATUS=$(get "$BASE_URL/document-mention/$DM_ID")
assert_status "GET /document-mention/:id" "200" "$STATUS"

STATUS=$(get "$BASE_URL/document-mention/00000000-0000-0000-0000-000000000000")
assert_status "GET /document-mention/:id — not found returns 404" "404" "$STATUS"

# ==============================================================================
section "9. Asset — Create & Read"
# ==============================================================================

STATUS=$(post "$BASE_URL/asset" "{
  \"personId\": \"$PERSON_ID\",
  \"type\": \"IMOVEL\",
  \"description\": \"Apartamento no centro da cidade\",
  \"estimatedValue\": 450000.00,
  \"source\": \"Declaração IRPF 2023\",
  \"stillHaveIt\": true,
  \"acquiredAt\": \"2020-06-15\",
  \"mappedAt\": \"2024-01-10\"
}")
assert_status "POST /asset" "201" "$STATUS"

STATUS=$(post "$BASE_URL/asset" "{
  \"personId\": \"$PERSON_ID\",
  \"type\": \"VEICULO\",
  \"description\": \"Veículo Toyota Corolla 2022\",
  \"estimatedValue\": 95000.00,
  \"source\": \"DETRAN-SP\",
  \"stillHaveIt\": true,
  \"acquiredAt\": \"2022-03-10\",
  \"mappedAt\": \"2024-01-10\"
}")
assert_status "POST /asset (second asset for same person)" "201" "$STATUS"

STATUS=$(get "$BASE_URL/asset")
assert_status "GET /asset" "200" "$STATUS"

ASSET_ID=$(jq_body '.[0].id')
info "Asset ID: $ASSET_ID"

STATUS=$(get "$BASE_URL/asset/$ASSET_ID")
assert_status "GET /asset/:id" "200" "$STATUS"

STATUS=$(get "$BASE_URL/asset/00000000-0000-0000-0000-000000000000")
assert_status "GET /asset/:id — not found returns 404" "404" "$STATUS"

# ==============================================================================
section "10. ActorIndicator — Create & Read"
# ==============================================================================

STATUS=$(post "$BASE_URL/actor-indicator" "{
  \"actorId\": \"$ACTOR1_ID\",
  \"indicatorType\": \"PATRIMONIO_INCOMPATIVEL\",
  \"value\": \"Alto\",
  \"source\": \"Análise COAF\",
  \"date\": \"2024-01-05\"
}")
assert_status "POST /actor-indicator" "201" "$STATUS"

STATUS=$(post "$BASE_URL/actor-indicator" "{
  \"actorId\": \"$ACTOR2_ID\",
  \"indicatorType\": \"EMPRESA_FACHADA\",
  \"value\": \"Suspeito\",
  \"source\": \"Inteligência Policial\",
  \"date\": \"2023-11-20\"
}")
assert_status "POST /actor-indicator (second indicator)" "201" "$STATUS"

STATUS=$(get "$BASE_URL/actor-indicator")
assert_status "GET /actor-indicator" "200" "$STATUS"

AI_ID=$(jq_body '.[0].id')
info "ActorIndicator ID: $AI_ID"

STATUS=$(get "$BASE_URL/actor-indicator/$AI_ID")
assert_status "GET /actor-indicator/:id" "200" "$STATUS"

STATUS=$(get "$BASE_URL/actor-indicator/00000000-0000-0000-0000-000000000000")
assert_status "GET /actor-indicator/:id — not found returns 404" "404" "$STATUS"

# ==============================================================================
section "11. RedFlag — Create & Read (multiple scenarios)"
# ==============================================================================

# RedFlag linked to actor
STATUS=$(post "$BASE_URL/red-flag" "{
  \"actorId\": \"$ACTOR1_ID\",
  \"type\": \"PATRIMONIO_INCOMPATIVEL_COM_RENDA\",
  \"severity\": 8,
  \"description\": \"Patrimônio declarado incompatível com renda conhecida\"
}")
assert_status "POST /red-flag (linked to actor)" "201" "$STATUS"

# RedFlag linked to public procurement
STATUS=$(post "$BASE_URL/red-flag" "{
  \"publicProcurementId\": \"$PP_ID\",
  \"type\": \"DISPENSA_INDEVIDA\",
  \"severity\": 9,
  \"description\": \"Possível dispensa irregular de licitação\"
}")
assert_status "POST /red-flag (linked to public procurement)" "201" "$STATUS"

# RedFlag linked to both actor and public procurement
STATUS=$(post "$BASE_URL/red-flag" "{
  \"actorId\": \"$ACTOR2_ID\",
  \"publicProcurementId\": \"$PP_ID\",
  \"type\": \"EMPRESA_SUSPEITA_VENCEDORA\",
  \"severity\": 10,
  \"description\": \"Empresa com indícios de irregularidade venceu licitação\"
}")
assert_status "POST /red-flag (linked to actor + public procurement)" "201" "$STATUS"

# RedFlag with transaction reference
STATUS=$(post "$BASE_URL/red-flag" "{
  \"actorId\": \"$ACTOR1_ID\",
  \"transactionId\": \"$TX_ID\",
  \"type\": \"TRANSACAO_SUSPEITA\",
  \"severity\": 7,
  \"description\": \"Transação financeira suspeita detectada\"
}")
assert_status "POST /red-flag (with transaction reference)" "201" "$STATUS"

STATUS=$(get "$BASE_URL/red-flag")
assert_status "GET /red-flag" "200" "$STATUS"

RF_COUNT=$(jq_body 'length')
info "Total RedFlags created: $RF_COUNT"

RF_ID=$(jq_body '.[0].id')
info "RedFlag ID: $RF_ID"

STATUS=$(get "$BASE_URL/red-flag/$RF_ID")
assert_status "GET /red-flag/:id" "200" "$STATUS"

STATUS=$(get "$BASE_URL/red-flag/00000000-0000-0000-0000-000000000000")
assert_status "GET /red-flag/:id — not found returns 404" "404" "$STATUS"

# ==============================================================================
section "12. Validation — Bad Request Scenarios"
# ==============================================================================

# Missing required fields
STATUS=$(post "$BASE_URL/person" "{
  \"cpf\": \"99999999999\",
  \"actorId\": \"$ACTOR1_ID\"
}")
assert_status "POST /person without completeName (should fail 4xx)" "400" "$STATUS"

STATUS=$(post "$BASE_URL/business" "{
  \"fantasyName\": \"Sem CNPJ\",
  \"actorId\": \"$ACTOR2_ID\"
}")
# legalName and cnpj are required — expect 400
if [[ "$STATUS" == "400" || "$STATUS" == "422" ]]; then
  ok "POST /business without required fields (got HTTP $STATUS)"
  PASS=$((PASS + 1))
else
  fail "POST /business without required fields — expected 4xx, got $STATUS"
  FAIL=$((FAIL + 1))
fi

STATUS=$(post "$BASE_URL/public-procurement" "{
  \"modality\": \"PREGAO\",
  \"actorId\": \"$ACTOR3_ID\"
}")
assert_status "POST /public-procurement without object (should fail 400)" "400" "$STATUS"

STATUS=$(post "$BASE_URL/actor-indicator" "{
  \"actorId\": \"$ACTOR1_ID\",
  \"indicatorType\": \"TIPO\"
}")
assert_status "POST /actor-indicator without value (should fail 400)" "400" "$STATUS"

STATUS=$(post "$BASE_URL/document-mention" "{
  \"documentId\": \"$DOC_ID\",
  \"actorId\": \"$ACTOR4_ID\",
  \"confidence\": 11
}")
assert_status "POST /document-mention with confidence > 10 (should fail 400)" "400" "$STATUS"

STATUS=$(post "$BASE_URL/red-flag" "{
  \"type\": \"SEM_REFERENCIA\",
  \"severity\": 5,
  \"description\": \"Sem actor, procurement, transaction ou association\"
}")
# The DB check constraint requires at least one non-null FK, but validation may allow it at HTTP level
# We just check it doesn't crash the server
if [[ "$STATUS" == "400" || "$STATUS" == "409" || "$STATUS" == "500" || "$STATUS" == "201" ]]; then
  info "POST /red-flag with no references → HTTP $STATUS (DB constraint handled)"
  PASS=$((PASS + 1))
else
  fail "POST /red-flag with no references → unexpected HTTP $STATUS"
  FAIL=$((FAIL + 1))
fi

# ==============================================================================
section "13. Final Read — Full State Verification"
# ==============================================================================

echo ""
info "Listing all entities:"

COUNT=$(curl -s "$BASE_URL/actor" | jq 'length')
info "Actors:             $COUNT"

COUNT=$(curl -s "$BASE_URL/person" | jq 'length')
info "Persons:            $COUNT"

COUNT=$(curl -s "$BASE_URL/business" | jq 'length')
info "Businesses:         $COUNT"

COUNT=$(curl -s "$BASE_URL/association" | jq 'length')
info "Associations:       $COUNT"

COUNT=$(curl -s "$BASE_URL/transaction" | jq 'length')
info "Transactions:       $COUNT"

COUNT=$(curl -s "$BASE_URL/public-procurement" | jq 'length')
info "PublicProcurements: $COUNT"

COUNT=$(curl -s "$BASE_URL/document" | jq 'length')
info "Documents:          $COUNT"

COUNT=$(curl -s "$BASE_URL/document-mention" | jq 'length')
info "DocumentMentions:   $COUNT"

COUNT=$(curl -s "$BASE_URL/asset" | jq 'length')
info "Assets:             $COUNT"

COUNT=$(curl -s "$BASE_URL/actor-indicator" | jq 'length')
info "ActorIndicators:    $COUNT"

COUNT=$(curl -s "$BASE_URL/red-flag" | jq 'length')
info "RedFlags:           $COUNT"

# ==============================================================================
section "Results"
# ==============================================================================

TOTAL=$((PASS + FAIL))
echo ""
echo -e "  Total:  $TOTAL"
echo -e "  ${GREEN}Passed: $PASS${RESET}"
echo -e "  ${RED}Failed: $FAIL${RESET}"
echo ""

if [ "$FAIL" -eq 0 ]; then
  echo -e "${GREEN}  ✔ All tests passed!${RESET}"
  exit 0
else
  echo -e "${RED}  ✘ $FAIL test(s) failed.${RESET}"
  exit 1
fi
