public class ProductManager {
    private ProductRepository prodRepo;

    public ProductManager(ProductRepository prodRepo) {
        this.prodRepo = prodRepo;
    }


    public Product[] returnAllProductsInDB() { /** выводит все объекты продуктов, имеющиеся в репозитории */
        Product[] productDatabase = prodRepo.getProductDatabase();
        return productDatabase;
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