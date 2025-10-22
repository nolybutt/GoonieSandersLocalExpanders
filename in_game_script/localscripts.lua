local p = peripheral.wrap("GoonieSandersLocalExpanders")
if not p then error("Codex Loader peripheral not found!") end

local scripts = p.listScripts()
print("Available scripts:")
for i, s in ipairs(scripts) do print(i .. ". " .. s) end

write("Select > ")
local choice = tonumber(read())
local name = scripts[choice]
if not name then error("Invalid choice") end

local content = p.getScript(name)
fs.makeDir("/scripts")
local f = fs.open("/scripts/" .. name, "w")
f.write(content)
f.close()

print("Saved to /scripts/" .. name)
write("Run now? (y/n) ")
if read() == "y" then shell.run("/scripts/" .. name) end
