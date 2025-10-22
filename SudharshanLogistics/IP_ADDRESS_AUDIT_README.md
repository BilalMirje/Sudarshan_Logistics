# IP Address Audit Tracking Implementation

## Overview

This implementation adds automatic IP address tracking to all audit fields in your SudharshanLogistics application. The system captures client IP addresses and stores them in the `ipAddress` field of the `Audit` entity whenever entities are created or updated.

## What Was Implemented

### 1. RequestContext.java
- Thread-local storage for IP addresses per HTTP request
- Ensures IP addresses are available during entity lifecycle operations
- Thread-safe implementation

### 2. AuditEntityListener.java
- JPA Entity Listener that automatically sets IP addresses
- Triggers on `@PrePersist` and `@PreUpdate` operations
- Only sets valid IP addresses (not "unknown" or null)

### 3. IpAuditConfig.java
- Spring Web Interceptor that captures IP addresses from HTTP requests
- Uses your existing `IpAddressUtil` to handle various proxy scenarios
- Stores IP address in `RequestContext` for the duration of the request

### 4. WebConfig.java
- Registers the IP audit interceptor for all endpoints (`/**`)
- Ensures IP addresses are captured for every HTTP request

### 5. Updated Audit.java
- Added `AuditEntityListener` to the `@EntityListeners` annotation
- Now automatically tracks IP addresses for all entities extending `Audit`

## How It Works

1. **Request Processing**: When an HTTP request arrives, `IpAuditConfig` interceptor captures the client IP address using `IpAddressUtil.getClientIpAddress()`

2. **IP Storage**: The IP address is stored in `RequestContext` (ThreadLocal) for the duration of the request

3. **Entity Operations**: When entities extending `Audit` are saved (created/updated), `AuditEntityListener` automatically retrieves the IP address from `RequestContext` and sets it in the `ipAddress` field

4. **Database Storage**: The IP address is persisted to the database along with other audit information

## Benefits

- **Automatic**: No manual intervention required - works for all entities extending `Audit`
- **Comprehensive**: Handles various proxy scenarios (X-Forwarded-For, Cloudflare, etc.)
- **Thread-Safe**: Uses ThreadLocal to ensure IP addresses don't leak between requests
- **Non-Intrusive**: Existing code continues to work without modifications

## Testing

The system includes comprehensive unit tests (`IpAddressAuditTest.java`) that verify:
- IP addresses are correctly set on entity creation and updates
- Invalid IP addresses ("unknown", null) are properly handled
- ThreadLocal behavior works correctly
- Request context isolation between different requests

## Usage

The system works automatically for all entities that extend `Audit`, including:
- `AppUser`
- `Branch`
- `Consignment`
- `Driver`
- `Vehicle`
- And all other entities extending `Audit`

## OpenAPI Server Configuration

**Important Note**: The IP address `192.168.1.50:8585` in your `OpenApiConfig.java` is used only for Swagger UI documentation and does **NOT** affect the actual IP address tracking in audit fields. The audit system captures the real client IP addresses from HTTP requests, not the OpenAPI server configuration.

## Example

When a user creates a new `AppUser` through your API:
1. The HTTP request arrives with the client's real IP address
2. `IpAuditConfig` captures and stores the IP address
3. When the `AppUser` entity is saved, `AuditEntityListener` automatically sets the `ipAddress` field
4. The IP address is stored in the database along with `createdAt`, `createdBy`, etc.

This ensures complete audit trail with both user information and IP address tracking.
