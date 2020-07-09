package cloud.gouyiba.core.module;

/**
 * this class by created wuyongfei on 2020/5/10 23:09
 **/
public class MybatisMaxPlugVersion {

    private final static String DEFAULT_VERSION = "1.0.3-RELEASE";

    private MybatisMaxPlugVersion() {
    }

    public static String getVersion() {
        Package pkg = MybatisMaxPlugVersion.class.getPackage();
        return pkg != null && pkg.getImplementationVersion() != null ? pkg.getImplementationVersion() : DEFAULT_VERSION;
    }
}
