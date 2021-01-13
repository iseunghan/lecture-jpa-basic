package dialect;

import org.hibernate.dialect.H2Dialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StandardBasicTypes;

/**
 * H2Dialect 를 상속받아서 내가 사용하고 싶은 함수를 등록 시킬 수 있다.
 * <p>
 * 어떻게 생성하는지는 H2Dialect 클래스 들어가서 registerFunction 메소드를 보고 참조하면 된다.
 */
public class MyH2Dialect extends H2Dialect {
    public MyH2Dialect() {
        registerFunction("group_concat", new StandardSQLFunction("group_concat", StandardBasicTypes.STRING));
    }
}
