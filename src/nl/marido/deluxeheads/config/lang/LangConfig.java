package nl.marido.deluxeheads.config.lang;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.configuration.ConfigurationSection;

import nl.marido.deluxeheads.DeluxeHeads;
import nl.marido.deluxeheads.config.ConfigFile;
import nl.marido.deluxeheads.config.FileConfigFile;
import nl.marido.deluxeheads.util.Clock;

public class LangConfig {

	private final ConfigFile configFile;
	private Map<String, LangMessage> defaults;
	private Map<String, LangMessage> messages;

	public LangConfig() {
		this.configFile = new FileConfigFile("lang.yml");

		reload();
	}

	public void reload() {
		Clock timer = Clock.start();

		this.configFile.copyDefaults();
		this.configFile.reload();

		ConfigurationSection defaultConfig = this.configFile.getDefaults();

		this.defaults = load(defaultConfig);

		ConfigurationSection config = this.configFile.getConfig();

		this.messages = load(config);

		boolean save = false;

		for (Entry<String, LangMessage> def : this.defaults.entrySet()) {
			if (!this.messages.containsKey(def.getKey())) {
				DeluxeHeads.warning("\"lang.yml\" is missing key \"" + def.getKey() + "\", adding it");

				config.set(def.getKey(), def.getValue().getConfigSaveValue());
				this.messages.put(def.getKey(), def.getValue());
				save = true;
			}
		}

		if (save) {
			this.configFile.save();
		}

		DeluxeHeads.info("Loaded Lang File with " + this.messages.size() + " messages " + timer);
	}

	private Map<String, LangMessage> load(ConfigurationSection sec) {
		Map<String, LangMessage> map = new HashMap<>();

		for (String key : sec.getKeys(false)) {
			if (sec.isConfigurationSection(key)) {
				map.putAll(load(sec.getConfigurationSection(key)));
				continue;
			}

			String pathKey = (sec.getCurrentPath().isEmpty() ? key : sec.getCurrentPath() + "." + key);

			if (sec.isList(key)) {
				List<String> lines = sec.getStringList(key);

				map.put(pathKey, new LangMessage(lines.toArray(new String[0])));
			} else if (sec.isString(key)) {
				map.put(pathKey, new LangMessage(sec.getString(key)));
			} else {
				DeluxeHeads.warning("Unable to load message at \"" + pathKey + "\", was not text or a list of text.");
			}
		}

		return map;
	}

	public LangMessage getMessage(String key) {
		LangMessage message = this.messages.get(key);
		return (message == null ? this.defaults.get(key) : message);
	}
}
