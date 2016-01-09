package ru.whalemare.database;

public class Index {

    private int index; // индекс
    private Item item; // ccылка на item

    Index(Item item){
        this.item = item; // записали ссылку на запись
        this.index++; // увеличили индекс
    }

}
