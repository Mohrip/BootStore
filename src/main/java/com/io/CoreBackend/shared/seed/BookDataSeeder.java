package com.io.CoreBackend.shared.seed;

import com.io.CoreBackend.authors.entity.Author;
import com.io.CoreBackend.book.entity.Book;
import com.io.CoreBackend.book.entity.Category;
import com.io.CoreBackend.authors.repository.AuthorRepository;
import com.io.CoreBackend.book.repository.BookRepository;
import com.io.CoreBackend.book.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

@Component
@Profile("seed")
public class BookDataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(BookDataSeeder.class);

    private static final int TARGET_BOOK_COUNT = 18000000;
    private static final int BATCH_SIZE = 500;

    private static final String[] ADJECTIVES = {
            "Abyssal", "Acoustic", "Aeon", "Aerial", "Ageless", "Alabaster", "Amber", "Ambient", "Amethyst", "Ancestral",
            "Ancient", "Angelic", "Anomalous", "Aquatic", "Archaic", "Arcane", "Arctic", "Ardent", "Argent", "Ashen",
            "Astral", "Atomic", "Audacious", "Auroral", "Autumnal", "Azure", "Barren", "Bejeweled", "Beloved", "Bitter",
            "Blazing", "Bleak", "Blissful", "Bloodbound", "Brass", "Brave", "Broken", "Bronze", "Burning", "Calm",
            "Captive", "Cataclysmic", "Cavernous", "Celestial", "Cerulean", "Chilling", "Chronological", "Cinder", "Cobalt", "Colossal",
            "Coral", "Corrupt", "Cosmic", "Crimson", "Crying", "Cryptic", "Crystalline", "Cursed", "Damned", "Darkling",
            "Dauntless", "Defiant", "Delicate", "Deserted", "Devout", "Diamond", "Dire", "Distant", "Divine", "Doomladen",
            "Dorian", "Dread", "Dreaming", "Dusky", "Earthen", "Eastbound", "Echoing", "Eclipsed", "Eldritch", "Electric",
            "Elegant", "Elemental", "Emerald", "Enchanted", "Endless", "Ethereal", "Eternal", "Evocative", "Exalted", "Exiled",
            "Faded", "Fading", "Fabled", "Fallen", "Feared", "Feral", "Fiery", "Flaring", "Flawless", "Forbidden",
            "Forgotten", "Forsaken", "Fortified", "Fossilized", "Fragile", "Frigid", "Frostbitten", "Frozen", "Gilded", "Glacial",
            "Glimmering", "Gleaming", "Glowing", "Golden", "Gossamer", "Grand", "Grave", "Grim", "Grizzled", "Hallowed",
            "Haunted", "Heavy", "Hidden", "Hollow", "Humming", "Hyper", "Icy", "Ignited", "Illusive", "Immortal",
            "Imperial", "Incandescent", "Infinite", "Infamous", "Intrepid", "Iron", "Ivory", "Jade", "Jagged", "Kinetic",
            "Luminous", "Lunar", "Lurking", "Lyrical", "Magnetic", "Majestic", "Malevolent", "Malicious", "Mammoth", "Marble",
            "Maroon", "Matted", "Melancholy", "Merciless", "Metallic", "Midnight", "Mistbound", "Misty", "Molten", "Monolithic",
            "Moonlit", "Mournful", "Murmuring", "Mystic", "Mythic", "Nebulous", "Neon", "Nether", "Noble", "Nomadic",
            "Nordic", "Northern", "Obsidian", "Oceanic", "Ominous", "Opal", "Orbiting", "Ornate", "Overt", "Pagan",
            "Pale", "Paranormal", "Pelagic", "Perilous", "Petrified", "Phantom", "Placid", "Plague", "Platinum", "Polar",
            "Primal", "Primeval", "Prismatic", "Pristine", "Prone", "Prophetic", "Proud", "Pyre", "Quantum", "Quiet",
            "Radiant", "Raging", "Rebel", "Regal", "Relentless", "Resolute", "Resonant", "Restless", "Revered", "Rift",
            "Ritual", "Royal", "Ruby", "Runic", "Rustic", "Ruthless", "Sacred", "Saffron", "Sage", "Sapphire",
            "Savage", "Scarlet", "Scorched", "Secret", "Seismic", "Serene", "Shadowy", "Shamanic", "Sharded", "Shattered",
            "Shimmering", "Shrouded", "Silent", "Silver", "Sinister", "Skeletal", "Solar", "Solitary", "Somber", "Sonic",
            "Sovereign", "Spectral", "Spiral", "Splendid", "Spooky", "Starlit", "Starry", "Stellar", "Stalwart", "Stormy",
            "Subterranean", "Sullen", "Sunken", "Surreal", "Sylvan", "Synthetic", "Tacit", "Tainted", "Temporal", "Tempestuous",
            "Thermal", "Thorned", "Thunderous", "Timeless", "Titan", "Titanic", "Tormented", "Toxic", "Tranquil", "Treacherous",
            "Triumphant", "Twilight", "Twinkling", "Typhoon", "Umbrial", "Unbroken", "Uncharted", "Underground", "Unholy", "Untamed",
            "Vagrant", "Valiant", "Vanguard", "Vaporous", "Veiled", "Velvet", "Velvety", "Vengeful", "Venomous", "Verdant",
            "Vibrant", "Victorious", "Vile", "Vintage", "Violent", "Vivid", "Vocal", "Void", "Volcanic", "Vortex",
            "Vulnerable", "Wandering", "Wasted", "Weathered", "Weeping", "Whispering", "White", "Wild", "Windblown", "Winter",
            "Withered", "Wondrous", "Wooden", "Wounded", "Wraithlike", "Wretched", "Zenith", "Zephyr", "Zodiac", "Zircon"
    };


    private static final String[] NOUNS = {
            "Abyss", "Academy", "Altar", "Anchor", "Anomaly", "Apex", "Aqueduct", "Arch", "Archipelago", "Arena",
            "Armor", "Arsenal", "Artifact", "Asylum", "Atoll", "Atlas", "Aura", "Automaton", "Avalanche", "Avenue",
            "Axis", "Badge", "Balcony", "Banner", "Barony", "Barracks", "Basin", "Bastion", "Bay", "Bayou",
            "Bazaar", "Beacon", "Bell", "Belt", "Birthplace", "Blade", "Blight", "Blossom", "Border", "Borough",
            "Boulder", "Boundary", "Bower", "Branch", "Bridge", "Brook", "Burrow", "Cabin", "Cage", "Cairn",
            "Camp", "Canal", "Canopy", "Canyon", "Capital", "Capitol", "Caravan", "Cascade", "Castle", "Catacomb",
            "Cathedral", "Cave", "Cavern", "Cell", "Cenotaph", "Centrifuge", "Chain", "Chalice", "Chamber", "Channel",
            "Chasm", "Chateau", "Chest", "Chiefdom", "Citadel", "City", "Clan", "Cliff", "Cloister", "Cloud",
            "Cluster", "Coast", "Colony", "Colossus", "Column", "Compass", "Conclave", "Conduit", "Continent", "Core",
            "Corridor", "Cottage", "Courtyard", "Cove", "Crater", "Crest", "Crevice", "Crown", "Crypt", "Cube",
            "Current", "Cycle", "Dam", "Den", "Depot", "Desert", "Dial", "Domain", "Dome", "Dominion",
            "Doorway", "Dream", "Dune", "Dungeon", "Dynasty", "Eagle", "Echo", "Eclipse", "Eden", "Edge",
            "Embers", "Emblem", "Empire", "Enclave", "Engine", "Epicenter", "Estuary", "Eye", "Facade", "Factory",
            "Falcon", "Falls", "Farm", "Fjord", "Flame", "Fleet", "Footprint", "Ford", "Forest", "Forge",
            "Fort", "Fortress", "Fountain", "Fox", "Frontier", "Furnace", "Gale", "Gallow", "Garden", "Garrison",
            "Gateway", "Geyser", "Ghost town", "Glacier", "Glade", "Glow", "Gorge", "Grail", "Grange", "Grid",
            "Grotto", "Grove", "Gully", "Hacienda", "Hall", "Hamlet", "Harbor", "Hearth", "Heartland", "Hedge",
            "Helm", "Hemisphere", "Highland", "Hill", "Hive", "Horizon", "Hound", "House", "Hub", "Island",
            "Isle", "Isthmi", "Jungle", "Keep", "Kingdom", "Laboratory", "Labyrinth", "Lagoon", "Lair", "Lake",
            "Lantern", "Lattice", "Ledge", "Legacy", "Library", "Lighthouse", "Lineage", "Locomotive", "Loom", "Lowland",
            "Mansion", "Manor", "Market", "Matrix", "Meadow", "Mesa", "Metropolis", "Mill", "Mine", "Mirage",
            "Mirror", "Monastery", "Monument", "Moor", "Mountain", "Mound", "Museum", "Nation", "Nebula", "Necklace",
            "Nest", "Network", "Nexus", "Niche", "Nucleus", "Oasis", "Observatory", "Ocean", "Orbit", "Orchard",
            "Outpost", "Overlook", "Palace", "Panther", "Passage", "Path", "Pathway", "Pavilion", "Peak", "Peninsula",
            "Perch", "Pharos", "Phenomenon", "Pillar", "Pinnacle", "Pipeline", "Plaza", "Plateau", "Pond", "Portal",
            "Prism", "Promontory", "Province", "Pyramid", "Quarry", "Quarter", "Quasar", "Radar", "Rail", "Ranch",
            "Rapids", "Ravine", "Realm", "Redoubt", "Reef", "Refuge", "Regime", "Relic", "Reservoir", "Resort",
            "Ridge", "Rift", "Ring", "River", "Road", "Rock", "Rookery", "Rose", "Route", "Ruins",
            "Rune", "Sanctuary", "Saturn", "Scarp", "Scepter", "School", "Scroll", "Sea", "Sector", "Sentinel",
            "Serpent", "Shadow", "Shard", "Shield", "Shipyard", "Shore", "Shrine", "Silo", "Singularity", "Siren",
            "Site", "Skyline", "Slope", "Solitude", "Song", "Sound", "Source", "Sphere", "Spike", "Spire",
            "Spring", "Square", "Stadium", "Star", "Station", "Steppe", "Stockade", "Storm", "Strand", "Stream",
            "Stronghold", "Studio", "Summit", "Supernova", "Swamp", "Symmetry", "Synagogue", "Tabernacle", "Tavern", "Temple",
            "Tempest", "Terminal", "Terrace", "Territory", "Theater", "Throne", "Tide", "Titan", "Tomb", "Tome",
            "Tor", "Tower", "Town", "Track", "Trail", "Trench", "Tributary", "Trove", "Tundra", "Tunnel",
            "Turret", "University", "Vale", "Valley", "Vanguard", "Vault", "Vessel", "Viper", "Void", "Volcano",
            "Vortex", "Wall", "Ward", "Warehouse", "Wasteland", "Watchtower", "Waterfall", "Wave", "Wellspring", "Whirlpool",
            "Wilds", "Willow", "Windmill", "Wolf", "Woods", "Yard", "Zenith", "Zephyr", "Ziggurat", "Zone"
    };



    private static final String[] SUBJECTS = {
            "Ambition", "Ancestors", "Angels", "Anguish", "Apathy", "Apparitions", "Artifice", "Ascent", "Ash", "Ashes",
            "Auras", "Autonomy", "Awakening", "Beauty", "Belief", "Betrayal", "Blades", "Blizards", "Blood", "Bones",
            "Bravery", "Burdens", "Candles", "Carnage", "Catastrophes", "Caverns", "Chains", "Chaos", "Charity", "Chills",
            "Cinders", "Clarity", "Clouds", "Comets", "Conquests", "Conspiracies", "Conscience", "Constellations", "Corruption", "Courage",
            "Covenants", "Creation", "Creatures", "Crows", "Crowns", "Curses", "Cycles", "Darkness", "Dawns", "Deceit",
            "Deception", "Defiance", "Delusions", "Despair", "Destinies", "Devotion", "Diamonds", "Dimensions", "Discovery", "Disdain",
            "Divinity", "Domination", "Dominion", "Doom", "Dragons", "Dreams", "Dust", "Dynasties", "Echoes", "Eclipses",
            "Elements", "Elixirs", "Embers", "Emires", "Emotions", "Empires", "Envy", "Essence", "Eternity", "Exile",
            "Existences", "Fables", "Faith", "Falcons", "Fallacy", "Famine", "Fantasy", "Fate", "Feathers", "Fears",
            "Flames", "Flesh", "Fools", "Forgiveness", "Fortitude", "Fortune", "Freedom", "Fury", "Galaxies", "Garments",
            "Generations", "Genius", "Ghosts", "Glaciers", "Glass", "Gloom", "Glory", "Glyphs", "Graves", "Grief",
            "Guardians", "Guilt", "Harmony", "Hatred", "Heirs", "Honour", "Hope", "Horrors", "Hunger", "Identity",
            "Idols", "Illusion", "Illusions", "Infamy", "Innocence", "Insight", "Irony", "Isolations", "Jealousy", "Judgments",
            "Justice", "Kings", "Kingdoms", "Lanterns", "Laughter", "Legacy", "Legends", "Lies", "Light", "Lightning",
            "Logic", "Loneliness", "Lords", "Loss", "Love", "Loyalty", "Madness", "Malice", "Martyrs", "Melodies",
            "Memory", "Mercy", "Miracles", "Mirages", "Misery", "Monarchs", "Moons", "Mortality", "Mysteries", "Mythology",
            "Nightmares", "Nobility", "Novas", "Oaths", "Oblivion", "Obsession", "Origins", "Outcasts", "Paradoxes", "Passions",
            "Phantoms", "Philosophy", "Plagues", "Portals", "Power", "Prisons", "Prophecies", "Purity", "Ravens", "Rebellion",
            "Regrets", "Relics", "Remnants", "Requiems", "Revenge", "Riddles", "Rituals", "Roses", "Ruins", "Runes",
            "Sacrifice", "Salvation", "Sanctity", "Saviors", "Scepters", "Scrolls", "Secrets", "Serpents", "Shards", "Shadows",
            "Shields", "Shivers", "Sigils", "Silence", "Sinners", "Sins", "Solitude", "Sorcery", "Sorrow", "Sorrows",
            "Sparks", "Specters", "Spirits", "Stars", "Stone", "Storms", "Substance", "Suffering", "Tears", "Tempests",
            "Terror", "Thorns", "Thrones", "Thunder", "Tides", "Time", "Tokens", "Tombs", "Tomes", "Tragedies",
            "Treason", "Treads", "Trials", "Tribute", "Triumph", "Truth", "Twilights", "Tyranny", "Universe", "Valor",
            "Vengeance", "Visions", "Voids", "Vows", "Vipers", "Warriors", "Whispers", "Wilds", "Winter", "Wisdom",
            "Wishes", "Witchcraft", "Wolves", "Wonders", "Wrath", "Wraiths", "Yearning", "Youth", "Zeal", "Zeniths"
    };


    private static final String[] FIRST_NAMES = {
            "James", "Mary", "Robert", "Patricia", "John", "Jennifer", "Michael", "Linda",
            "William", "Elizabeth", "David", "Barbara", "Richard", "Susan", "Joseph", "Jessica",
            "Thomas", "Sarah", "Charles", "Karen",
            "Daniel", "Margaret", "Matthew", "Dorothy", "Anthony", "Emily", "Mark", "Frances",
            "Steven", "Amanda", "Paul", "Donna", "Andrew", "Ruth", "Joshua", "Carol",
            "Kenneth", "Sharon", "Kevin", "Michelle", "Brian", "Laura", "George", "Sarah",
            "Edward", "Kimberly", "Ronald", "Deborah", "Timothy", "Jessica", "Jason", "Shirley", "Jeffrey", "Cynthia",
            "Alexander", "Victoria", "Sebastian", "Eleanor", "Gabriel", "Scarlett", "Julian", "Anastasia",
            "Theodore", "Evangeline", "Dominic", "Genevieve", "Adrian", "Juliet", "Xavier", "Vivian",


    };

    private static final String[] LAST_NAMES = {
            "Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis",
            "Rodriguez", "Martinez", "Hernandez", "Lopez", "Gonzalez", "Wilson", "Anderson", "Thomas",
            "Taylor", "Moore", "Jackson", "Martin", "Lee", "Thompson", "White", "Harris", "Sanchez", "Clark", "Ramirez", "Lewis",
            "Robinson", "Walker", "Young", "Allen", "King", "Wright", "Scott", "Torres",
            "Hawthorne", "Blackwood", "Montague", "Sterling", "Pendleton", "Harrington", "Kingsley", "Davenport",
            "Sinclair", "Covington", "Fairchild", "Gallowglass", "Beaumont", "Wellington", "Ashford", "Fitzgerald",
            "Cross", "Mercer", "Hayes", "Vane", "Stark", "Phoenix", "Rhodes", "Blair",
            "Wilder", "Sage", "Foster", "Rowe", "Chase", "Knox", "Gage", "Nash",


    };

    private static final String[] CATEGORY_NAMES = {
            "Fiction", "Non-Fiction", "Science Fiction", "Fantasy", "Mystery",
            "Biography", "History", "Romance", "Horror", "Thriller",
            "Poetry", "Self-Help", "Children's", "Young Adult", "Science",
            "Philosophy", "Travel", "Cooking", "Art", "Business",
            "Health", "Religion", "Sports", "Music", "Education",
            "Technology", "Politics", "Environment", "Culture", "Psychology",
            "Adventure", "Crime", "Drama", "Satire", "Classic",
            "Memoir", "Anthology", "Short Stories", "Graphic Novel", "Western",
            "Dystopian", "Historical Fiction", "Urban Fiction", "Magical Realism"
    };

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final Random random = new Random(42);

    public BookDataSeeder(BookRepository bookRepository, AuthorRepository authorRepository,
                           CategoryRepository categoryRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) {
        long existing = bookRepository.count();
        if (existing >= TARGET_BOOK_COUNT) {
            log.info("Skipping seed: {} books already present (target {}).", existing, TARGET_BOOK_COUNT);
            return;
        }

        List<Author> authors = seedAuthors();
        List<Category> categories = seedCategories();

        long totalTitleSpace = (long) ADJECTIVES.length * NOUNS.length * SUBJECTS.length;
        if (totalTitleSpace < TARGET_BOOK_COUNT) {
            log.error("Only {} unique titles available, but target is {}.", totalTitleSpace, TARGET_BOOK_COUNT);
            return;
        }
        long[] indices = shuffledIndices(totalTitleSpace);

        log.info("Seeding {} books into the database...", TARGET_BOOK_COUNT);

        List<Book> batch = new ArrayList<>(BATCH_SIZE);
        int inserted = 0;
        for (int i = 0; i < TARGET_BOOK_COUNT; i++) {
            Book book = new Book();
            book.setTitle(titleForIndex(indices[i]));
            book.setIsbn(generateIsbn(i));
            book.setPrice(randomPrice());
            book.setStockCount(random.nextInt(501));
            book.setPublicationDate(randomPublicationDate());
            book.setAuthor(authors.get(random.nextInt(authors.size())));
            book.setCategory(random.nextInt(10) == 0 ? null : categories.get(random.nextInt(categories.size())));
            batch.add(book);

            if (batch.size() == BATCH_SIZE) {
                bookRepository.saveAll(batch);
                inserted += batch.size();
                batch.clear();
                log.info("Inserted {}/{} books", inserted, TARGET_BOOK_COUNT);
            }
        }
        if (!batch.isEmpty()) {
            bookRepository.saveAll(batch);
            inserted += batch.size();
        }

        log.info("Done. Inserted {} books, {} authors, {} categories.", inserted, authors.size(), categories.size());
    }

    private List<Author> seedAuthors() {
        Set<String> names = new LinkedHashSet<>();
        for (String first : FIRST_NAMES) {
            for (String last : LAST_NAMES) {
                names.add(first + " " + last);
            }
        }
        List<String> nameList = new ArrayList<>(names);
        Collections.shuffle(nameList, random);

        List<Author> authors = new ArrayList<>();
        for (int i = 0; i < Math.min(300, nameList.size()); i++) {
            authors.add(Author.builder().name(nameList.get(i)).build());
        }
        return authorRepository.saveAll(authors);
    }

    private List<Category> seedCategories() {
        List<Category> categories = new ArrayList<>();
        for (String name : CATEGORY_NAMES) {
            categories.add(Category.builder().name(name).build());
        }
        return categoryRepository.saveAll(categories);
    }

    private String titleForIndex(long index) {
        int subjectIdx = (int) (index % SUBJECTS.length);
        index /= SUBJECTS.length;
        int nounIdx = (int) (index % NOUNS.length);
        index /= NOUNS.length;
        int adjectiveIdx = (int) (index % ADJECTIVES.length);

        return ADJECTIVES[adjectiveIdx] + " " + NOUNS[nounIdx] + " of " + SUBJECTS[subjectIdx];
    }

    private long[] shuffledIndices(long totalSpace) {
        long[] indices = new long[(int) totalSpace];
        for (int i = 0; i < indices.length; i++) {
            indices[i] = i;
        }
        for (int i = indices.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            long tmp = indices[i];
            indices[i] = indices[j];
            indices[j] = tmp;
        }
        return indices;
    }

    private String generateIsbn(int index) {
        return "978" + String.format("%010d", index);
    }

    private BigDecimal randomPrice() {
        int cents = 500 + random.nextInt(14500); // 5.00 - 150.00
        return BigDecimal.valueOf(cents, 2).setScale(2, RoundingMode.HALF_UP);
    }

    private LocalDate randomPublicationDate() {
        int daysBack = random.nextInt(30 * 365);
        return LocalDate.now().minusDays(daysBack);
    }
}
