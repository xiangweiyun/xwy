package ${packageName};

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ${pojoPackageName}.${info.className};
import cn.trasen.mcpc.framework.base.OperContext;

/**
 * ${info.comments}服务测试.
 * 
 * @author ${author}
 * @date: ${currentDate}
 * @Copyright: Copyright (c) 2006 - ${currentYear}
 * @Company: XWY有限公司
 * @Version: V1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ${info.className}ServiceTest {
    @Autowired
    private ${info.className}Service ${info.varClassName}Service;

    @Test
    public void add() {
        ${info.className} ${info.varClassName} = new ${info.className}();
        ${info.varClassName}Service.add(OperContext.getSystemOperContext(), ${info.varClassName});
    }

    @Test
    public void modifyById() {
        ${info.className} ${info.varClassName} = new ${info.className}();
        ${info.varClassName}.setId(null);
        ${info.varClassName}Service.modifyById(OperContext.getSystemOperContext(), ${info.varClassName});
    }

    @Test
    public void removeById() {
        Long id = null;
        ${info.varClassName}Service.removeById(OperContext.getSystemOperContext(), id);
    }

    @Test
    public void getById() {
        Long id = null;
        ${info.className} ${info.varClassName} = ${info.varClassName}Service.getById(id);
        Assert.assertNotNull(${info.varClassName});
    }
}