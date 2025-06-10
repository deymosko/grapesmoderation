# 📜 Command List

```bash
/warn <player>         # Issues a warning to a player (permission level 2+)
/warnings              # View your own warnings (permission level 0)
/warnings <player>     # View another player's warnings (permission level 2+)
```
**Warning expiration rules:**
- Warnings expire automatically after **7 days**.
- Getting a new warning **resets all current timers**.
- After a player is unbanned *(ban time configured in `warn-common.toml`)*, **all warnings expire in 7 days**.
