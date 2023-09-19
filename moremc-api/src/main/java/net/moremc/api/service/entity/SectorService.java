package net.moremc.api.service.entity;

import net.moremc.api.API;
import net.moremc.api.sector.type.SectorType;
import net.moremc.api.sector.Sector;
import net.moremc.api.service.ServiceImpl;

import java.util.Optional;

public class SectorService extends ServiceImpl<String, Sector> {


    public Sector findSectorByType(SectorType sectorType){
        return this.getMap()
                .values()
                .stream()
                .filter(sector -> sector.getInfo().getType().equals(sectorType))
                .findFirst()
                .orElse(null);
    }
    public Sector findSectorOnlineByType(SectorType sectorType){
        return this.getMap()
                .values()
                .stream()
                .filter(sector -> sector.getInfo().getType().equals(sectorType))
                .filter(sector -> sector.getInfo().isOnline())
                .findFirst()
                .orElse(null);
    }
    public Optional<Sector> findRandomSectorOnlineByType(SectorType sectorType){
        return this.getMap()
                .values()
                .stream()
                .filter(sector -> sector.getInfo().getType().equals(sectorType))
                .findFirst();
    }

    public Optional<Sector> getCurrentSector() {
        return this.findByValueOptional(API.getInstance().getSectorName());
    }

    public Optional<Sector> findSectorByLocation(String world, int blockX, int blockZ) {
        return this.getMap()
                .values()
                .stream()
                .filter(sector -> sector.getInfo().getLocationInfo().isInSector(blockX, blockZ, world))
                .findFirst();
    }

    public boolean isOnlinePlayer(String nickName) {
        return this.getMap().values()
                .stream()
                .anyMatch(sector -> sector.getInfo().getPlayerList().contains(nickName));
    }
    public int getOnline() {
        return this.getMap().values().stream().mapToInt(sector -> sector.getInfo().getPlayerList().size()).sum();
    }
}
