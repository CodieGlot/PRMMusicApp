#!/bin/bash

# === Configuration ===
declare -a AUTHORS=(
  "huy.nguyen <ngnhathuy1224@gmail.com>"
  "quy.duong <quyduong0304@gmail.com>"
  "vu.ho <htv22122004@gmail.com>"
  "thanh.dang <thanhdhde170795@fpt.edu.vn>"
  "thanh.manh <mthanh2337@gmail.com>"
)

declare -a MESSAGES=(
  "Merge pull request #%s into branch main"
  "fix bugs"
  "add playlist and songs"
  "add songs"
  "Fix bug in modules"
  "refactor firebase logic"
  "Update code"
  "Remove deprecated code"
  "Fix typo"
)

START_DATE="2024-06-20"
TOTAL_COMMITS=20

# === Utilities ===
pick_random_author() {
  AUTHOR="${AUTHORS[$RANDOM % ${#AUTHORS[@]}]}"
  NAME=$(echo "$AUTHOR" | sed 's/ <.*//')
  EMAIL=$(echo "$AUTHOR" | sed 's/.*<\(.*\)>/\1/')
}

pick_random_message() {
  RAW_MSG="${MESSAGES[$RANDOM % ${#MESSAGES[@]}]}"
  # Replace %s with commit number if used
  MESSAGE=$(printf "$RAW_MSG" "$1")
}

# === Initialize repo if not already initialized ===
if [ ! -d .git ]; then
  git init
  git checkout -B main
  git add -A
  git commit -m "Initial commit"
fi

# === Ensure we're on main branch ===
git checkout -B main

# === Create fake commits ===
for i in $(seq 1 $TOTAL_COMMITS); do
  pick_random_author
  pick_random_message "$i"

  # Generate random hour, minute, and second
HOUR=$((RANDOM % 24))
MINUTE=$((RANDOM % 60))
SECOND=$((RANDOM % 60))

# Construct full datetime string with random time
DATE=$(date -d "$START_DATE +$((i-1)) days $HOUR:$MINUTE:$SECOND" +"%Y-%m-%dT%H:%M:%S")

  git commit --allow-empty -m "$MESSAGE" \
    --author="$NAME <$EMAIL>" \
    --date="$DATE"
done
# === Done ===
echo -e "\n✅ $TOTAL_COMMITS fake commits created on 'main'.\n"
