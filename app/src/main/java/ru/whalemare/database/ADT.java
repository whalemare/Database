package ru.whalemare.database;

import java.util.ArrayList;

/**
 *  АТД - абстрактный тип данных.
 */

public class ADT {

    final public static String TAG = "WHALETAG"; // для отладки

    private ArrayList<Item> items = new ArrayList<>(); // блок записей

    private Block block = new Block(); // блок индексов
    private ArrayList<Block> blocks = new ArrayList<>();

    private Item item; // запись
    private Index index; // индекс

    int i = 0; // счетчик блоков

    public ADT() {

    };

    public boolean add(Item item)
    {
        boolean answer;

        this.item = item;
        this.index = new Index(item); // передали в индекс ссылку на запись

        //TODO проверка: существует ли уже заносимая запись в списке? y: не разрешать запись

        if (block.getSize() <= 40) // если в блоке еще осталось >20% места
            block.add(index); // запишем в этот блок индекс
        else // как только место закончилось
        {
            blocks.add(block); // занесем в список блоков наш сформированный блок
            blocks.get(i).add(index); // последний индекс занесем в этот же блок, уже находящийся в массиве
            block = null; // обнулим все что сейчас находится в блоке
            i++; // подвинем счетчик
        }

        items.add(item);

        answer = true;

        return answer;
    }

    /**
    *  Функция `search` производит поиск одинаковых записей в БД (что ищем, где ищем)
    *  @param item объект типа Item, который мы ищем
    *  @param items список объектов в котором производится поиск
     * @return true - если элемента нет,
     *         false - если элемент нашелся.

    private boolean search(Item item, ArrayList<Item> items)
    {
        for (int i = 0; i < items.size(); i++)
            if (items.get(i).getName().equals(item.getName()))
                return false;
        return true;
    }

     */
}
