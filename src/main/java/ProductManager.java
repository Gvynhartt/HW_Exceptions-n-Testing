public class ProductManager {
    private ProductRepository prodRepo;

    public ProductManager(ProductRepository prodRepo) {
        this.prodRepo = prodRepo;
    }

    public void addProductToRepo(Product productEntry) throws AlreadyExistsException { /** добавляет объект продукта в репозиторий */
        Product[] productDatabase = prodRepo.getProductDatabase();

        for (Product targetProd : productDatabase) {
            if (targetProd.getProdId() == productEntry.getProdId()) {
                throw new AlreadyExistsException("Товар с таким ID (" + targetProd.getProdId() + ") уже добавлен в БД");
            }
        }

        Product[] bufferDatabase = new Product[productDatabase.length + 1];
        for (int pos = 0; pos < productDatabase.length; pos++) {
            bufferDatabase[pos] = productDatabase[pos];
        }
        bufferDatabase[bufferDatabase.length - 1] = productEntry;
        prodRepo.setProductDatabase(bufferDatabase);
    }

    public Product[] returnAllProductsInDB() { /** выводит все объекты продуктов, имеющиеся в репозитории */
        Product[] productDatabase = prodRepo.getProductDatabase();
        return productDatabase;
    }

    public void deleteFromDBbyID(int targetId) throws NotFoundException { /** удаляет объект продукта из репозитория по ID */
        Product[] productDatabase = prodRepo.getProductDatabase();

        /** дополняем наш метод описанием исключительного сценария */
        if (findById(targetId) == null) {
            throw new NotFoundException("Объект с указанным ID (" + targetId + ") отсутствует в базе данных");
        }

        int matchCount = 0;
        for (Product newProdEntry : productDatabase) {
            if (newProdEntry.getProdId() == targetId) {
                matchCount++;
            }
        }

        int pos = 0;
        int buffPos = 0;
        Product[] bufferDatabase = new Product[productDatabase.length - matchCount];
        for (Product newProdEntry : productDatabase) {
            if (newProdEntry.getProdId() != targetId) {
                bufferDatabase[buffPos] = productDatabase[pos];
                pos++;
                buffPos++;
            } else {
                pos++;
            }
        }
        prodRepo.setProductDatabase(bufferDatabase);
    }

    public Product findById(int targetId) { /** добавленный метод поиска в БД по идентияикатору */
        Product[] productDatabase = prodRepo.getProductDatabase();

        for (Product targetProd : productDatabase) {
            if (targetProd.getProdId() == targetId) {
                return targetProd;
            }
        }
        return null;
    }

    public Product[] searchByText(String queryText) { /** сличает запрос с полем "prodName" и группирует найденные объекты продуктов в массив */
        Product[] productDatabase = prodRepo.getProductDatabase();
        Product[] bufferDatabase = new Product[productDatabase.length];
        int pos = 0;
        int matchCount = 0;

        for (Product targetProd : productDatabase) {
            if (targetProd.matchesQuery(queryText) == true) {
                bufferDatabase[pos] = targetProd;
                pos++;
                matchCount++;
            } else {
                pos++;
            }
        }

        Product[] resultDatabase = new Product[matchCount];
        int posResult = 0;
        pos = 0;

        for (pos = 0; pos < bufferDatabase.length; pos++) {
            if (bufferDatabase[pos] != null) {
                resultDatabase[posResult] = bufferDatabase[pos];
                posResult++;
            }
        }
        return resultDatabase;
    }
}