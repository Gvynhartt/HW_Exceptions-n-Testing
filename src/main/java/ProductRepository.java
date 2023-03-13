public class ProductRepository {
    private Product[] productDatabase = new Product[0];

    public Product[] getProductDatabase() {
        return productDatabase;
    }

    public void setProductDatabase(Product[] productDatabase) {
        this.productDatabase = productDatabase;
    }

    public void addProductToRepo(Product productEntry) throws AlreadyExistsException { /** добавляет объект продукта в репозиторий */
        //Product[] productDatabase = getProductDatabase();

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
        setProductDatabase(bufferDatabase);;
    }

    public void deleteFromDBbyID(int targetId) throws NotFoundException { /** удаляет объект продукта из репозитория по ID */
        Product[] productDatabase = getProductDatabase();

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
        setProductDatabase(bufferDatabase);
    }


    public Product findById(int targetId) { /** добавленный метод поиска в БД по ID */
        Product[] productDatabase = getProductDatabase();

        for (Product targetProd : productDatabase) {
            if (targetProd.getProdId() == targetId) {
                return targetProd;
            }
        }
        return null;
    }
}
