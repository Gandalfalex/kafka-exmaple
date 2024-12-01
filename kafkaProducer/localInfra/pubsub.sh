#!/bin/bash
echo "Setting up Pub/Sub Emulator..."

# Set the emulator host for local development
export PUBSUB_EMULATOR_HOST=${PUBSUB_EMULATOR_HOST:-localhost:8085}

gcloud config set auth/disable_credentials true
gcloud config set project test-project

# Create topics
gcloud pubsub topics create request-topic
gcloud pubsub topics create response-topic

# Create subscriptions
gcloud pubsub subscriptions create request-subscription --topic=request-topic
gcloud pubsub subscriptions create response-subscription --topic=response-topic

echo "Pub/Sub Emulator setup complete!"
