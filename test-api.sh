#!/bin/bash

# JWT Authentication API Test Script
# This script demonstrates how to use the authentication endpoints

BASE_URL="http://localhost:8080"
USERNAME="testuser_$(date +%s)"
PASSWORD="TestPass123!"

echo "======================================"
echo "JWT Authentication API Test"
echo "======================================"
echo ""

# Color codes for output
GREEN='\033[0;32m'
RED='\033[0;31m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 1. Test Register
echo -e "${BLUE}1. Testing User Registration${NC}"
echo "POST $BASE_URL/auth/register"
REGISTER_RESPONSE=$(curl -s -X POST "$BASE_URL/auth/register" \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"$USERNAME\",\"password\":\"$PASSWORD\"}")

echo "Response:"
echo "$REGISTER_RESPONSE" | jq '.'
echo ""

# Check if registration was successful
if echo "$REGISTER_RESPONSE" | jq -e '.success == true' > /dev/null; then
    echo -e "${GREEN}✓ Registration successful${NC}"
else
    echo -e "${RED}✗ Registration failed${NC}"
fi
echo ""
echo "======================================"
echo ""

# 2. Test Login
echo -e "${BLUE}2. Testing User Login${NC}"
echo "POST $BASE_URL/auth/login"
LOGIN_RESPONSE=$(curl -s -X POST "$BASE_URL/auth/login" \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"$USERNAME\",\"password\":\"$PASSWORD\"}")

echo "Response:"
echo "$LOGIN_RESPONSE" | jq '.'
echo ""

# Extract tokens
ACCESS_TOKEN=$(echo "$LOGIN_RESPONSE" | jq -r '.data.accessToken')
REFRESH_TOKEN=$(echo "$LOGIN_RESPONSE" | jq -r '.data.refreshToken')

if [ "$ACCESS_TOKEN" != "null" ] && [ -n "$ACCESS_TOKEN" ]; then
    echo -e "${GREEN}✓ Login successful${NC}"
    echo "Access Token: ${ACCESS_TOKEN:0:50}..."
    echo "Refresh Token: ${REFRESH_TOKEN:0:50}..."
else
    echo -e "${RED}✗ Login failed${NC}"
    exit 1
fi
echo ""
echo "======================================"
echo ""

# 3. Test Protected Endpoint
echo -e "${BLUE}3. Testing Protected Endpoint${NC}"
echo "GET $BASE_URL/ (with JWT)"
PROTECTED_RESPONSE=$(curl -s -X GET "$BASE_URL/" \
  -H "Authorization: Bearer $ACCESS_TOKEN")

echo "Response:"
echo "$PROTECTED_RESPONSE"
echo ""

if [ -n "$PROTECTED_RESPONSE" ]; then
    echo -e "${GREEN}✓ Protected endpoint access successful${NC}"
else
    echo -e "${RED}✗ Protected endpoint access failed${NC}"
fi
echo ""
echo "======================================"
echo ""

# 4. Test Token Refresh
echo -e "${BLUE}4. Testing Token Refresh${NC}"
echo "POST $BASE_URL/auth/refresh"
REFRESH_RESPONSE=$(curl -s -X POST "$BASE_URL/auth/refresh" \
  -H "Content-Type: application/json" \
  -d "{\"refreshToken\":\"$REFRESH_TOKEN\"}")

echo "Response:"
echo "$REFRESH_RESPONSE" | jq '.'
echo ""

NEW_ACCESS_TOKEN=$(echo "$REFRESH_RESPONSE" | jq -r '.data.accessToken')
if [ "$NEW_ACCESS_TOKEN" != "null" ] && [ -n "$NEW_ACCESS_TOKEN" ]; then
    echo -e "${GREEN}✓ Token refresh successful${NC}"
    echo "New Access Token: ${NEW_ACCESS_TOKEN:0:50}..."
else
    echo -e "${RED}✗ Token refresh failed${NC}"
fi
echo ""
echo "======================================"
echo ""

# 5. Test Logout
echo -e "${BLUE}5. Testing Logout${NC}"
echo "POST $BASE_URL/auth/logout"
LOGOUT_RESPONSE=$(curl -s -X POST "$BASE_URL/auth/logout" \
  -H "Authorization: Bearer $ACCESS_TOKEN")

echo "Response:"
echo "$LOGOUT_RESPONSE" | jq '.'
echo ""

if echo "$LOGOUT_RESPONSE" | jq -e '.success == true' > /dev/null; then
    echo -e "${GREEN}✓ Logout successful${NC}"
else
    echo -e "${RED}✗ Logout failed${NC}"
fi
echo ""
echo "======================================"
echo ""

# 6. Test Invalid Credentials
echo -e "${BLUE}6. Testing Invalid Credentials${NC}"
echo "POST $BASE_URL/auth/login (with wrong password)"
INVALID_LOGIN=$(curl -s -X POST "$BASE_URL/auth/login" \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"$USERNAME\",\"password\":\"wrongpassword\"}")

echo "Response:"
echo "$INVALID_LOGIN" | jq '.'
echo ""

if echo "$INVALID_LOGIN" | jq -e '.success == false' > /dev/null; then
    echo -e "${GREEN}✓ Invalid credentials properly handled${NC}"
else
    echo -e "${RED}✗ Invalid credentials handling failed${NC}"
fi
echo ""
echo "======================================"
echo ""

# 7. Test Duplicate Registration
echo -e "${BLUE}7. Testing Duplicate Registration${NC}"
echo "POST $BASE_URL/auth/register (with existing username)"
DUPLICATE_RESPONSE=$(curl -s -X POST "$BASE_URL/auth/register" \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"$USERNAME\",\"password\":\"$PASSWORD\"}")

echo "Response:"
echo "$DUPLICATE_RESPONSE" | jq '.'
echo ""

if echo "$DUPLICATE_RESPONSE" | jq -e '.statusCode == 409' > /dev/null; then
    echo -e "${GREEN}✓ Duplicate registration properly handled${NC}"
else
    echo -e "${RED}✗ Duplicate registration handling failed${NC}"
fi
echo ""
echo "======================================"
echo ""

echo -e "${GREEN}All tests completed!${NC}"
