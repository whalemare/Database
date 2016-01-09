package ru.whalemare.database;

import java.util.ArrayList;

public class Block {

    ArrayList<Index> indexes;
    final int COUNT = 50;

    Block()
    {
        indexes = new ArrayList<>(COUNT);
    }

    public void add(Index index)
    {
        indexes.add(index);
    }

    public int getSize(){
        return indexes.size();
    }
}
