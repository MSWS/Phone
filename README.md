# Phone

Discord bot that proxies SMS messages to a phone number.
Uses Twilio to send and receive messages.

## Setup

Environment variables:

- DISCORD_TOKEN: The discord bot token to use
- TWILIO_ACCOUNT_SID: Twilio account SID
- TWILIO_AUTH_TOKEN: Twilio auth token
- TWILIO_SENDER: The Twilio phone number to send/receive messages from
- TWILIO_WEBHOOK: The webhook URL that twilio will POST to when a message is received