--
-- BSD-style license; for more info see http://pmd.sourceforge.net/license.html
--

CREATE OR REPLACE PROCEDURE test2 ( a OUT number, b OUT number )
 AS
 BEGIN
        FOR registro IN ( SELECT  SUM(field1),
                                  MAX(field2)
                            FROM  test_tbl
                            GROUP BY fieldx)
        LOOP
            null;
        END LOOP;
END test2;
