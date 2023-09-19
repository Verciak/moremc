package net.moremc.api;

import net.moremc.api.entity.repository.*;
import net.moremc.api.mysql.MySQL;
import net.moremc.api.nats.NatsMessengerAPI;
import net.moremc.api.nats.packet.server.callback.CallbackFactory;
import net.moremc.api.service.entity.*;
import org.nustaq.serialization.FSTConfiguration;

public class API
{

    private static API instance;

    private MySQL mySQL;
    private NatsMessengerAPI natsMessengerAPI;

    private final FSTConfiguration fstConfiguration = FSTConfiguration.createDefaultConfiguration();
    private final CallbackFactory callbackFactory = new CallbackFactory();

    private final SectorService sectorService = new SectorService();

    private final MasterService masterService = new MasterService();

    private final UserService userService = new UserService();
    private final UserRepository userRepository = new UserRepository();

    private final BanService banService = new BanService();
    private final BanRepository banRepository = new BanRepository();

    private final GuildService guildService = new GuildService();
    private final GuildRepository guildRepository = new GuildRepository();

    private final BackPackService backPackService = new BackPackService();

    private final KitService kitService = new KitService();
    private final KitRepository kitRepository = new KitRepository();

    private final AccountService accountService = new AccountService();
    private final AccountRepository accountRepository = new AccountRepository();

    private final BazaarService bazaarService = new BazaarService();
    private final BazaarRepository bazaarRepository = new BazaarRepository();

    private final ServerService serverService = new ServerService();
    private final ServerRepository serverRepository = new ServerRepository();

    private final GuildPlayerSelectAreaGeneratorService selectAreaGeneratorService = new GuildPlayerSelectAreaGeneratorService();

    private String sectorName;

    public static API getInstance() {
        return instance;
    }
    public API(boolean database) {
        instance = this;
        if(database){
            this.mySQL = new MySQL();
            this.bazaarRepository.initializeDatabase(this.mySQL, this.bazaarService);
            this.serverRepository.initializeDatabase(this.mySQL, this.serverService);
            this.userRepository.initializeDatabase(this.mySQL, this.userService);
            this.banRepository.initializeDatabase(this.mySQL, this.banService);
            this.guildRepository.initializeDatabase(this.mySQL, this.guildService);
            this.accountRepository.initializeDatabase(this.mySQL, this.accountService);
            this.kitRepository.initializeDatabase(this.mySQL, this.kitService);
        }
    }
    public void initialize(String packageNameRedis, String... channelListen){
        this.natsMessengerAPI  = new NatsMessengerAPI(channelListen, packageNameRedis);
    }
    public NatsMessengerAPI getNatsMessengerAPI() {
        return natsMessengerAPI;
    }

    public MySQL getMySQL() {
        return mySQL;
    }

    public KitRepository getKitRepository() {
        return kitRepository;
    }

    public BanRepository getBanRepository() {
        return banRepository;
    }
    public GuildRepository getGuildRepository() {
        return guildRepository;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public ServerService getServerService() {
        return serverService;
    }

    public ServerRepository getServerRepository() {
        return serverRepository;
    }

    public BazaarRepository getBazaarRepository() {
        return bazaarRepository;
    }

    public BazaarService getBazaarService() {
        return bazaarService;
    }

    public AccountRepository getAccountRepository() {
        return accountRepository;
    }

    public AccountService getAccountService() {
        return accountService;
    }

    public GuildPlayerSelectAreaGeneratorService getSelectAreaGeneratorService() {
        return selectAreaGeneratorService;
    }

    public BanService getBanService() {
        return banService;
    }

    public KitService getKitService() {
        return kitService;
    }

    public CallbackFactory getCallbackFactory() {
        return callbackFactory;
    }

    public SectorService getSectorService() {
        return sectorService;
    }

    public GuildService getGuildService() {
        return guildService;
    }

    public UserService getUserService() {
        return userService;
    }

    public BackPackService getBackPackService() {
        return backPackService;
    }

    public FSTConfiguration getFstConfiguration() {
        return fstConfiguration;
    }

    public String getSectorName() {
        return sectorName;
    }

    public void setSectorName(String sectorName) {
        this.sectorName = sectorName;
    }

    public MasterService getMasterService() {
        return masterService;
    }
}
