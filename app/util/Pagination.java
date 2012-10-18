package util;

/**
 * Created with IntelliJ IDEA.
 * User: ben
 * Date: 10/17/12
 * Time: 8:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class Pagination {
    public int page;
    public int length;

    public Pagination( int page, int length ) {
        this.page = page;
        this.length = length;
    }
}
