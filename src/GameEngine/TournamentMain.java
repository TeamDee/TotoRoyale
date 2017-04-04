package GameEngine;

import GameNetworking.TigerLandDelegate;

/**
 * Created by Z_K on 4/3/2017.
 */
public class TournamentMain {
    public static void main(String[] args){
        TigerLandDelegate delegate = new TigerLandDelegate("localhost", 6066);
        delegate.TournamentProtocol();
    }
}
