package com.challenge.danny.rentmydriveway.Service;

/**
 * Created by Danny on 10/29/18.
 */

public class ServiceEntity  {

    private boolean searching;
    private boolean hosting;

    public ServiceEntity(boolean searching, boolean hosting) {
        this.searching = searching;
        this.hosting = hosting;
    }

    public boolean isSearching() {
        return searching;
    }

    public boolean isHosting() {
        return hosting;
    }
}
