package models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ben
 * Date: 10/21/12
 * Time: 12:20 AM
 * To change this template use File | Settings | File Templates.
 */

public class Bill {
    public User from;
    public User to;
    public Float amount;

    public Bill() {}

    public Bill( User from, User to, Float amount ) {
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    public static List<Bill> generateByExpense( Expense expense ) {
        List<Bill> bills = new ArrayList<Bill>();

        for( Participant p : expense.participants ) {
            if( !p.user.equals( expense.payer )) {
                bills.add( new Bill( p.user, expense.payer, p.quota ) );
            }
        }

        return  bills;
    }

    public static List<Bill> generateByBowl( Bowl bowl ) {
        List<Bill> bills = new ArrayList<Bill>();
        List<Bill> retBills = new ArrayList<Bill>();

        List<Object[]> rawBills = Participant.find("" +
                " SELECT p1, p2, SUM(p.quota) " +
                " FROM Expense e " +
                " JOIN e.participants p " +
                " JOIN e.payer p2 " +
                " JOIN p.user p1 " +
                " WHERE e.bowl.id = ? AND p1 != p2 " +
                " GROUP BY p1, p2 " +
                " ORDER BY p1, p2, SUM(p.quota)",
                bowl.id ).fetch();

        for( Object[] row : rawBills ) {
            bills.add( new Bill( (User)row[0], (User)row[1], Float.valueOf(row[2].toString()) ) );
        }

        retBills.addAll( bills );

        Bill currentBill;
        Bill comparingBill;
        for( int i = 0; i < bills.size(); i++ ) {
            currentBill = bills.get( i );
            for( int j = i+1; j < bills.size(); j++ ) {
                comparingBill = bills.get( j );
                if( currentBill.from.equals( comparingBill.to ) ) {
                    if( currentBill.amount > comparingBill.amount ) {
                        currentBill.amount -= comparingBill.amount;
                        retBills.remove( comparingBill );
                    } else {
                        comparingBill.amount -= currentBill.amount;
                        retBills.remove( currentBill );
                    }
                    break;
                }
            }
        }
/*
        for( Expense expense : bowl.expenses ) {
            for( Participant p : expense.participants ) {
                if( !p.user.equals( expense.payer )) {
                    bills.add( new Bill( p.user, expense.payer, p.quota ) );
                }
            }
        }
*/

        return  retBills;
    }

}

