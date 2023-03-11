import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ProductManagerTest {

    ProductRepository prodRepo = new ProductRepository();
    ProductManager prodMngr = new ProductManager(prodRepo);
    Book book1 = new Book(1, "Kto vinovat?", 300, "Кто виноват?", "Герцен");
    Book book2 = new Book(2, "Chto delat'?", 907, "Что делать?", "Чернышевский");
    Book book3 = new Book(3, "Komu na Rusi zhit' khorosho", 410, "Кому на Руси жить хорошо", "Некрасов");
    Book book4 = new Book(4, "Chto, gde, kogda", 502, "Что, где, когда", "Мама дяди Фёдора");
    Book book5 = new Book(5, "Katekhizis revolyuqionera", 1917, "Катехизис революционера", "Нечаев");

    Smartphone phone1 = new Smartphone(6, "Yandex Voxel", 1612, "YV404Ы", "ZhuiPeng© Ltd");
    Smartphone phone2 = new Smartphone(7, "Nokia 3310", -300, "Kirpich", "ZhuiPeng© Ltd");
    Smartphone phone3 = new Smartphone(8, "Ural El'brus", 40000, "E2E4 80x86", "ZhuiPeng© Ltd");
    Smartphone phone4 = new Smartphone(9, "Yota Yoga Yoda Phone", 9001, "YU №69", "ZhuiPeng© Ltd");
    Smartphone phone5 = new Smartphone(10, "ojZvuk", 2147483647, "Prodaj Zhenu v JustChatting, Smerd", "ZhuiPeng© Ltd");

    Product product1 = new Product(11, "Lezerman Sudnogo Dnya", 39);
    Product product2 = new Product(12, "Sonic Screwdriver", 39);
    Product product3 = new Product(13, "Konfigurator", 39);
    Product product4 = new Product(13, "Klyuch ot kvartiry, gde den'gi lezhat", 39);

    /**
     * duplicate ID
     */

    @Test
    public void shdAddBookToDB() { /** проверяет добавление объекта из разных классов */
        prodMngr.addProductToRepo(book1);

        Product[] expected = {book1};
        Product[] actual = prodMngr.returnAllProductsInDB();

        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    public void shdAddPhoneToDB() { /** проверяет добавление объекта из разных классов */
        prodMngr.addProductToRepo(phone1);

        Product[] expected = {phone1};
        Product[] actual = prodMngr.returnAllProductsInDB();

        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    public void shdAddProductToDBifEmpty() { /** проверяет добавление объекта в пустое поле */
        prodMngr.addProductToRepo(product1);

        Product[] expected = {product1};
        Product[] actual = prodMngr.returnAllProductsInDB();

        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    public void shdAddProductToDBifNotEmpty() { /** проверяет добавление объекта в поле, если там уже содержатся объекты */
        Product[] productDatabase = {book3, phone5, product4};
        prodRepo.setProductDatabase(productDatabase);

        prodMngr.addProductToRepo(product1);

        Product[] expected = {book3, phone5, product4, product1};
        Product[] actual = prodMngr.returnAllProductsInDB();

        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    public void shdReturnAllObjInDB() { /** проверяет возврат всех объектов в репозитории */
        prodMngr.addProductToRepo(book3);
        prodMngr.addProductToRepo(book5);
        prodMngr.addProductToRepo(phone2);
        prodMngr.addProductToRepo(phone4);
        prodMngr.addProductToRepo(product3);

        Product[] expected = {book3, book5, phone2, phone4, product3};
        Product[] actual = prodMngr.returnAllProductsInDB();

        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    public void shdDeleteFromDBifSingle() { /** проверяет удаление одного объекта по ID, если он единственный в поле */
        prodMngr.addProductToRepo(phone5); /** удаляем phone5, ID: 10 */

        prodMngr.deleteFromDBbyID(10);

        Product[] expected = {};
        Product[] actual = prodMngr.returnAllProductsInDB();

        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    public void shdDeleteFromDBnormal() { /** проверяет удаление одного объекта по ID в "простом" виде */
        prodMngr.addProductToRepo(book3);
        prodMngr.addProductToRepo(book5);
        prodMngr.addProductToRepo(phone2);
        prodMngr.addProductToRepo(phone4); /** удаляем phone4, ID: 9 */
        prodMngr.addProductToRepo(product3);

        prodMngr.deleteFromDBbyID(9);

        Product[] expected = {book3, book5, phone2, product3};
        Product[] actual = prodMngr.returnAllProductsInDB();

        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    public void shdFindQueryMatchesInDB() { /** проверяет работу поиска в "простом" случае (по кратчайшему пути) */
        prodMngr.addProductToRepo(book1);
        prodMngr.addProductToRepo(book2);
        prodMngr.addProductToRepo(book3);
        prodMngr.addProductToRepo(book4);
        prodMngr.addProductToRepo(book5);

        Product[] expected = {book3};
        Product[] actual = prodMngr.searchByText("zhit'");

        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    public void shdFindQueryMatchesInDBMultiple() { /** проверяет работу поиска для множественных совпадений */
        prodMngr.addProductToRepo(book1); /** содержит символы "to" */
        prodMngr.addProductToRepo(book2); /** содержит символы "to" */
        prodMngr.addProductToRepo(book3);
        prodMngr.addProductToRepo(book4); /** содержит символы "to" */
        prodMngr.addProductToRepo(book5);

        Product[] expected = {book1, book2, book4};
        Product[] actual = prodMngr.searchByText("to");

        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    public void shdFindQueryMatchesInDBMultipleAcrossClasses() { /** проверяет работу поиска для множественных совпадений в разных классах */
        prodMngr.addProductToRepo(book2); /** содержит символы "de" */
        prodMngr.addProductToRepo(book4); /** содержит символы "de" */
        prodMngr.addProductToRepo(book3);
        prodMngr.addProductToRepo(phone1); /** содержит символы "de" */
        prodMngr.addProductToRepo(phone4);
        prodMngr.addProductToRepo(product2);
        prodMngr.addProductToRepo(product4); /** содержит символы "de" */

        Product[] expected = {book2, book4, phone1, product4};
        Product[] actual = prodMngr.searchByText("de");

        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    public void shdFindQueryMatchesInDBifNonexistent() { /** проверяет работу поиска, если нет совпадений с запросом */
        prodMngr.addProductToRepo(book2);
        prodMngr.addProductToRepo(book4);
        prodMngr.addProductToRepo(book3);
        prodMngr.addProductToRepo(phone1);
        prodMngr.addProductToRepo(phone4);
        prodMngr.addProductToRepo(product2);
        prodMngr.addProductToRepo(product4);

        Product[] expected = {};
        Product[] actual = prodMngr.searchByText("aboba");

        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    public void shdFindQueryMatchesInDBifCaseNotMatch() { /** проверяет работу поиска, если запрос набран в другом регистре */
        prodMngr.addProductToRepo(phone1);
        prodMngr.addProductToRepo(phone2);
        prodMngr.addProductToRepo(phone3);
        prodMngr.addProductToRepo(phone4);
        prodMngr.addProductToRepo(phone5);

        Product[] expected = {};
        Product[] actual = prodMngr.searchByText("yANDEX");

        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    public void shdFindQueryMatchesInDBifContainsSpaces() { /** проверяет работу поиска, если в запросе есть пробелы */
        prodMngr.addProductToRepo(phone1);
        prodMngr.addProductToRepo(phone2);
        prodMngr.addProductToRepo(phone3);
        prodMngr.addProductToRepo(phone4);
        prodMngr.addProductToRepo(phone5);

        Product[] expected = {phone4};
        Product[] actual = prodMngr.searchByText("Yota Yoga Yoda Phone");

        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    public void shdFindQueryMatchesInDBifContainsNumerics() { /** проверяет работу поиска, если в запросе содержатся цифры */
        prodMngr.addProductToRepo(phone1);
        prodMngr.addProductToRepo(phone2);
        prodMngr.addProductToRepo(phone3);
        prodMngr.addProductToRepo(phone4);
        prodMngr.addProductToRepo(phone5);

        Product[] expected = {phone2};
        Product[] actual = prodMngr.searchByText("3310");

        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    public void shdFindQueryMatchesInDBifContainsOtherSymbols() { /** проверяет работу поиска, если в запросе есть прочие символы */
        prodMngr.addProductToRepo(phone1);
        prodMngr.addProductToRepo(phone2);
        prodMngr.addProductToRepo(phone3);
        prodMngr.addProductToRepo(phone4);
        prodMngr.addProductToRepo(phone5);

        Product[] expected = {phone3};
        Product[] actual = prodMngr.searchByText("El'brus");

        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    /** #RICH */
    public void shdFindMatchesInSubClassBookNormal() { /** проверяет поиск в подклассе BOOK для доп. полей */
        prodMngr.addProductToRepo(book1);
        prodMngr.addProductToRepo(book2);
        prodMngr.addProductToRepo(book3);
        prodMngr.addProductToRepo(book4);
        prodMngr.addProductToRepo(book5);

        Product[] expected = {book5};
        Product[] actual = prodMngr.searchByText("Нечаев");

        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    /** #RICH */
    public void shdFindMatchesInSubClassBookIfNonexistent() { /** проверяет поиск в подклассе BOOK для доп. полей, если нет совпадений */
        prodMngr.addProductToRepo(book1);
        prodMngr.addProductToRepo(book2);
        prodMngr.addProductToRepo(book3);
        prodMngr.addProductToRepo(book4);
        prodMngr.addProductToRepo(book5);

        Product[] expected = {};
        Product[] actual = prodMngr.searchByText("Барков");

        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    /** #RICH */
    public void shdFindMatchesInSubClassPhoneNormal() { /** проверяет поиск в подклассе SMARTPHONE для доп. полей */
        prodMngr.addProductToRepo(phone1);
        prodMngr.addProductToRepo(phone2);
        prodMngr.addProductToRepo(phone3);
        prodMngr.addProductToRepo(phone4);
        prodMngr.addProductToRepo(phone5);

        Product[] expected = {phone1, phone2, phone3, phone4, phone5};
        Product[] actual = prodMngr.searchByText("Zhui");

        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    /** #RICH */
    public void shdFindMatchesInSubClassPhoneIfNonexistent() { /** проверяет поиск в подклассе SMARTPHONE для доп. полей, если нет совпадений */
        prodMngr.addProductToRepo(phone1);
        prodMngr.addProductToRepo(phone2);
        prodMngr.addProductToRepo(phone3);
        prodMngr.addProductToRepo(phone4);
        prodMngr.addProductToRepo(phone5);

        Product[] expected = {};
        Product[] actual = prodMngr.searchByText("Xiaomi");

        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    /** #RICH */
    public void shdFindMatchesAcrossSubclassesDifferentFields() { /** проверяет поиск во всех подклассах для доп. полей на разных этапах цикла */


        Book book6 = new Book(15, "Google - the Corp of All Evil", 2077, "Гугл - корпорация зла", "Стив Джобс");
        Smartphone phone6 = new Smartphone(16, "Pixel 100 500", 5928, "GP3450D", "Google Inc");

        prodMngr.addProductToRepo(book6);
        prodMngr.addProductToRepo(phone6);

        Product[] expected = {book6, phone6};
        Product[] actual = prodMngr.searchByText("Google");

        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    /** #NOTFOUND */
    public void shdFindInDBbyIDnormal() { /** сперва проверяем раобту "findById" с ID, имеющимся в БД */
        prodMngr.addProductToRepo(phone1);
        prodMngr.addProductToRepo(phone2);
        prodMngr.addProductToRepo(phone3); /** ищем этот объект по ID 8 */
        prodMngr.addProductToRepo(phone4);
        prodMngr.addProductToRepo(phone5);

        Product expected = phone3;
        Product actual = prodMngr.findById(8);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    /** #NOTFOUND */
    public void shdFindInDBbyIDifNonexistent() { /** потом проверяем раобту "findById" с ID, которого в БД нет */
        prodMngr.addProductToRepo(phone1);
        prodMngr.addProductToRepo(phone2);
        prodMngr.addProductToRepo(phone3);
        prodMngr.addProductToRepo(phone4);
        prodMngr.addProductToRepo(phone5);

        Product expected = null;
        Product actual = prodMngr.findById(21);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    /** #NOTFOUND */
    public void shdDeleteFromDBbyIDifFound() { /** проверяем работу метода после дополнения логики по позитивному сценарию */
        prodMngr.addProductToRepo(phone1);
        prodMngr.addProductToRepo(phone2);
        prodMngr.addProductToRepo(phone3); /** удалем этот объект по ID 8 */
        prodMngr.addProductToRepo(phone4);
        prodMngr.addProductToRepo(phone5);
        prodMngr.deleteFromDBbyID(8);

        Product[] expected = {phone1, phone2, phone4, phone5};
        Product[] actual = prodMngr.returnAllProductsInDB();

        Assertions.assertArrayEquals(expected, actual); /** т. к. проверяется нормальный вариант вызова метода*/
    }

    @Test
    /** #NOTFOUND */
    public void shdDeleteFromDBbyIDifNotFound() { /** собственно, тестируем метод на выброс требуемого исключения */
        prodMngr.addProductToRepo(phone1);
        prodMngr.addProductToRepo(phone2);
        prodMngr.addProductToRepo(phone3);
        prodMngr.addProductToRepo(phone4);
        prodMngr.addProductToRepo(phone5);

        Assertions.assertThrows(NotFoundException.class, () -> {
            prodMngr.deleteFromDBbyID(21);
        });
    }

    @Test
    /** #ALREADYEXISTS */
    public void shdAddProductToDBwithIDcheck() { /** проверяет добавление объекта в репозиторий с отслеживанием повтора ID */
        prodMngr.addProductToRepo(book1);
        prodMngr.addProductToRepo(book2);
        prodMngr.addProductToRepo(book3);
        prodMngr.addProductToRepo(book4);
        prodMngr.addProductToRepo(book5);

        Product[] expected = {book1, book2, book3, book4, book5};
        Product[] actual = prodRepo.getProductDatabase();

        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    /** #ALREADYEXISTS */
    public void shdAddProductToDBwithIDcheckException() { /** проверяет выброс исключения при дублировании ID */
        prodMngr.addProductToRepo(book1);
        prodMngr.addProductToRepo(book2);
        prodMngr.addProductToRepo(book3);
        prodMngr.addProductToRepo(book4);
        prodMngr.addProductToRepo(book5);

        Assertions.assertThrows(AlreadyExistsException.class, () -> {prodMngr.addProductToRepo(book5);});
    }
}
