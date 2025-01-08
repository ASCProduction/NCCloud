#!/bin/bash
PROJECT_FOLDER=$(dirname "$0")
cd "${PROJECT_FOLDER}" && pwd
cd "fastlane" && pwd
bundle install && bundle exec fastlane ios ios_release