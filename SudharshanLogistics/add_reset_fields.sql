-- Add reset token fields to app_user table
ALTER TABLE app_user
ADD COLUMN reset_token VARCHAR(255),
ADD COLUMN reset_token_expiry DATETIME;
