package Data;
import java.util.List;

public class Data {
    // import com.fasterxml.jackson.databind.ObjectMapper; // version 2.11.1
    // import com.fasterxml.jackson.annotation.JsonProperty; // version 2.11.1
    /*
     * ObjectMapper om = new ObjectMapper(); Root root = om.readValue(myJsonString),
     * Root.class);
     */
    public class E {
        public int abilityLevel;
        public String displayName;
        public String id;
        public String rawDescription;
        public String rawDisplayName;
    }

    public class Passive {
        public String displayName;
        public String id;
        public String rawDescription;
        public String rawDisplayName;
    }

    public class Q {
        public int abilityLevel;
        public String displayName;
        public String id;
        public String rawDescription;
        public String rawDisplayName;
    }

    public class R {
        public int abilityLevel;
        public String displayName;
        public String id;
        public String rawDescription;
        public String rawDisplayName;
    }

    public class W {
        public int abilityLevel;
        public String displayName;
        public String id;
        public String rawDescription;
        public String rawDisplayName;
    }

    public class Abilities {
        public E e;
        public Passive passive;
        public Q q;
        public R r;
        public W w;
    }

    public class ChampionStats {
        public double abilityPower;
        public double armor;
        public double armorPenetrationFlat;
        public double armorPenetrationPercent;
        public double attackDamage;
        public double attackRange;
        public double attackSpeed;
        public double bonusArmorPenetrationPercent;
        public double bonusMagicPenetrationPercent;
        public double cooldownReduction;
        public double critChance;
        public double critDamage;
        public double currentHealth;
        public double healthRegenRate;
        public double lifeSteal;
        public double magicLethality;
        public double magicPenetrationFlat;
        public double magicPenetrationPercent;
        public double magicResist;
        public double maxHealth;
        public double moveSpeed;
        public double physicalLethality;
        public double resourceMax;
        public double resourceRegenRate;
        public String resourceType;
        public double resourceValue;
        public double spellVamp;
        public double tenacity;
    }

    public class GeneralRune {
        public String displayName;
        public int id;
        public String rawDescription;
        public String rawDisplayName;
    }

    public class Keystone {
        public String displayName;
        public int id;
        public String rawDescription;
        public String rawDisplayName;
    }

    public class PrimaryRuneTree {
        public String displayName;
        public int id;
        public String rawDescription;
        public String rawDisplayName;
    }

    public class SecondaryRuneTree {
        public String displayName;
        public int id;
        public String rawDescription;
        public String rawDisplayName;
    }

    public class StatRune {
        public int id;
        public String rawDescription;
    }

    public class FullRunes {
        public List<GeneralRune> generalRunes;
        public Keystone keystone;
        public PrimaryRuneTree primaryRuneTree;
        public SecondaryRuneTree secondaryRuneTree;
        public List<StatRune> statRunes;
    }

    public class ActivePlayer {
        public Abilities abilities;
        public ChampionStats championStats;
        public double currentGold;
        public FullRunes fullRunes;
        public int level;
        public String summonerName;
    }

    public class Runes {
        public Keystone keystone;
        public PrimaryRuneTree primaryRuneTree;
        public SecondaryRuneTree secondaryRuneTree;
    }

    public class Scores {
        public int assists;
        public int creepScore;
        public int deaths;
        public int kills;
        public double wardScore;
    }

    public class SummonerSpellOne {
        public String displayName;
        public String rawDescription;
        public String rawDisplayName;
    }

    public class SummonerSpellTwo {
        public String displayName;
        public String rawDescription;
        public String rawDisplayName;
    }

    public class SummonerSpells {
        public SummonerSpellOne summonerSpellOne;
        public SummonerSpellTwo summonerSpellTwo;
    }

    public class AllPlayer {
        public String championName;
        public boolean isBot;
        public boolean isDead;
        public List<Object> items;
        public int level;
        public String position;
        public String rawChampionName;
        public double respawnTimer;
        public Runes runes;
        public Scores scores;
        public int skinID;
        public String summonerName;
        public SummonerSpells summonerSpells;
        public String team;
    }

    public class Event {
        public int eventID;
        public String eventName;
        public double eventTime;
    }

    public class Events {
        public List<Event> events;
    }

    public class GameData {
        public String gameMode;
        public double gameTime;
        public String mapName;
        public int mapNumber;
        public String mapTerrain;
    }

    public class Root {
        public ActivePlayer activePlayer;
        public List<AllPlayer> allPlayers;
        public Events events;
        public GameData gameData;
    }

}
