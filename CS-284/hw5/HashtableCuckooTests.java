package hw5;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.assertNull;

public class HashtableCuckooTests
{
    private HashtableCuckoo<String,Character> ht;

    @Test
    @DisplayName("Test checking values added to Cuckoo structure")
    void test1()
    {
        ht = new HashtableCuckoo<String, Character>();
        ht.put("adb",'a');
        ht.put("dba",'b');
        ht.put("pnc",'c');
        ht.put("ncp",'d');
        ht.put("cnp",'e');
        ht.put("wze",'f');
        ht.put("zew",'g');
        ht.put("kgt",'h');
        ht.put("tkg",'i');
        ht.put("srh",'j');
        ht.put("rhs",'k');
        StringBuilder result = new StringBuilder();
        result.append("[12,zew,g,table1]\n");
        result.append("[21,cnp,e,table1]\n");
        result.append("[33,rhs,k,table1]\n");
        result.append("[42,wze,f,table1]\n");
        result.append("[51,ncp,d,table1]\n");
        result.append("[55,adb,a,table1]\n");
        result.append("[56,tkg,i,table1]\n");
        result.append("[5,pnc,c,table2]\n");
        result.append("[9,dba,b,table2]\n");
        result.append("[17,srh,j,table2]\n");
        result.append("[25,kgt,h,table2]\n");
        assertEquals(result.toString(), ht.toString());
    }

    /* Method to add values to the Cuckoo structure for the following tests */
    void fillValues()
    {
        ht = new HashtableCuckoo<String, Character>();
        String[] arr1 = {"a" , "p", "w", "t", "s"};
        String[] arr2 = {"d", "n", "z", "k", "r"};
        String[] arr3 = {"b", "c", "e", "g", "h"};

        for(int i=0; i<5; i++)
        {
            String str = arr1[i] + arr2[i] + arr3[i];
            ht.put(str,'a');
            String str1 = arr2[i] + arr3[i] + arr1[i];
            ht.put(str1,'b');
            String str2 = arr3[i] + arr2[i] + arr1[i];
            ht.put(str2,'c');
        }

    }

    @Test
    @DisplayName("Test 2 (Check put with key existing in the Cuckoo structure ")
    void test2()
    {
        fillValues();
        ht.put("bda", 'g');
        assertEquals('g',(char)ht.get("bda"));

    }

    @Test
    @DisplayName("Test 3(Tests remove() method)")
    void test3()
    {
        fillValues();
        ht.remove("srh");
        assertNull(ht.get("srh"));

    }

    @Test
    @DisplayName("Tests get() method")
    void test4()
    {
        fillValues();
        assertEquals('a',(char)ht.get("srh"));

    }
    @Test
    @DisplayName("Tests remove() method")
    void test5()
    {
        fillValues();
        ht.remove("kgt");
        ht.remove("hrs");
        assertNull(ht.get("hrs"));

    }

    @Test
    @DisplayName("Combination test")
    void test6()
    {
        fillValues();
        ht.remove("kgt");
        ht.remove("hrs");
        ht.put("tkg", 'q');
        assertEquals('q', (char)ht.get("tkg"));
    }

    @Test
    @DisplayName("Tests remove() method")
    void test7()
    {
        fillValues();
        ht.remove("kgt");
        ht.remove("hrs");
        assertEquals(13,ht.size());
    }

    @Test
    @DisplayName("Tests size() method")
    void test8()
    {
        fillValues();
        assertEquals(15,ht.size());
    }

    @Test
    @DisplayName("Tests get() method")
    void test9()
    {
        fillValues();
        assertEquals('a',(char)ht.get("tkg"));

    }



}