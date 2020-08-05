
  
# Report build status next to bitbucket commit.
# 
# - Fill in APP_CENTER_ORG & APP_NAME with your organization and appname in appcenter.
# - Fill in BITBUCKET_ORG & REPOSITORY_SLUG with your Bitbucket organization and repo slug
# - Also provide BITBUCKET_CREDENTIALS env variable in build config (ex. username:password)
#   You can create a user who has only repository level access specifically for this purpose
#   and add that user to the organization that way you don't need to expose your own personal
#   credentials.
#
# Contributed by: DrBeak1
# https://bitbucket.org/DrBeak1/
# Copyright 2019 Zume, Inc.
# SPDX Identifier: MIT

APP_CENTER_ORG=TechSwivel
BITBUCKET_ORG=techswivel
APP_NAME=Udeo Globe
REPOSITORY_SLUG=p0021g-android-app


BUILD_URL=https://appcenter.ms/orgs/$APP_CENTER_ORG/apps/$APP_NAME/build/branches/$APPCENTER_BRANCH/builds/$APPCENTER_BUILD_ID

bitbucket_update_status() {
	if [ "$AGENT_JOBSTATUS" != "Succeeded" ]; then
	    bitbucket_set_status_failed
	else
	    bitbucket_set_status_success
	fi
}

bitbucket_set_status() {
	local status job_status
	local "${@}"

	if [ ! -z "$BITBUCKET_CREDENTIALS" ]; then
		curl -u $BITBUCKET_CREDENTIALS \
			-X POST \
			-H "Content-Type: application/json" \
			-d \
			"{
				\"state\": \"$status\",
				\"key\": \"$APPCENTER_BRANCH\",
				\"name\": \"$BUILD_REPOSITORY_NAME #$APPCENTER_BUILD_ID\",
				\"description\": \"The bulid status is: $job_status!\",
				\"url\": \"$BUILD_URL\"
			}" \
			-H "Content-Type: application/json" \
			-w "%{http_code}" \
			https://api.bitbucket.org/2.0/repositories/$BITBUCKET_ORG/$REPOSITORY_SLUG/commit/$BUILD_SOURCEVERSION/statuses/build
	else
		echo "Bitbucket credentials not found; skipping status update."
	fi
}

bitbucket_set_status_pending() {
	bitbucket_set_status status="INPROGRESS" job_status="In progress"
}

bitbucket_set_status_success() {
	bitbucket_set_status status="SUCCESSFUL" job_status="$AGENT_JOBSTATUS"
}

bitbucket_set_status_failed() {
	bitbucket_set_status status="FAILED" job_status="$AGENT_JOBSTATUS"
}
