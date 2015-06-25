package de.submit_ev.vendingapp.models;

/**
 * Created by Igor on 13.05.2015.
 */
public class Brand {
    Long id;
    
    String name;

    public Brand() {}

    public Brand(Long id) {
        setId(id);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
