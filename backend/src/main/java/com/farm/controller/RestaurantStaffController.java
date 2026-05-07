package com.farm.controller;

import com.farm.entity.*;
import com.farm.service.*;
import com.farm.entity.*;
import com.farm.entity.body.PageBody1;
import com.farm.entity.body.UserBodyChangePassword;
import com.farm.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/restaurantStaff")
// 餐厅员工功能
public class RestaurantStaffController {

    @Autowired
    private ReserveService reserveService;

    @Autowired
    private OrderingService orderingService;

    @Autowired
    private OrderingCompleteService orderingCompleteService;

    @Autowired
    private RepairService repairService;

    @Autowired
    private StaffService staffService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RestaurantMaterialService restaurantMaterialService;

    @Autowired
    private PurchaseService purchaseService;

    @GetMapping("/getReserve")
    // 获得所有订单信息
    public Map getReserve() {
        //System.out.println("get success!");

        int page = 1;
        int pages = 0;
        List<Integer> pageList = new ArrayList<Integer>();

        List<Reserve> reserveList = new ArrayList<Reserve>();
        reserveList = reserveService.findAllCustomerNotNull();

        if (reserveList.size() / 5 >= 5) {
            for (int i = 0; i < 5; i++) {
                pageList.add(i + 1);
            }

            if (reserveList.size() % 5 != 0) {
                pages = (reserveList.size() + 5) / 5;
            } else {
                pages = reserveList.size() / 5;
            }
            reserveList = reserveList.subList(0, 5);
        } else {
            if (!reserveList.isEmpty()) {
                if (reserveList.size() % 5 != 0) {
                    pages = (reserveList.size() + 5) / 5;
                } else {
                    pages = reserveList.size() / 5;
                }

                for (int i = 0; i < pages; i++) {
                    pageList.add(i + 1);
                }

                if (reserveList.size() < 5) {
                    reserveList = reserveList.subList(0, reserveList.size());
                } else {
                    reserveList = reserveList.subList(0, 5);
                }
            }
        }

        PageBody1 pageBody1 = new PageBody1();
        pageBody1.setPage(page);
        pageBody1.setPages(pages);
        pageBody1.setPageList(pageList);

        return Map.of("reserveList", reserveList, "pageBody1", pageBody1);
    }

    @PostMapping("/doPage")
    // 订单分页
    public Map doPage(@RequestBody PageBody1 pageBody1) {
        //System.out.println("post success!" + pageBody1.getPage());

        List<Integer> pageList = new ArrayList<Integer>();
        List<Reserve> reserveList = new ArrayList<Reserve>();

        reserveList = reserveService.findAllCustomerNotNull();
        if (reserveList.isEmpty()) {
            pageBody1.setPage(0);
            pageBody1.setPages(0);
            pageBody1.setPageList(pageList);
        } else {
            if ((double) reserveList.size() / 5 > 5.0) {
                if (reserveList.size() % 5 != 0) {
                    pageBody1.setPages((reserveList.size() + 5) / 5);
                } else {
                    pageBody1.setPages(reserveList.size() / 5);
                }

                if (pageBody1.getPage() <= pageBody1.getPages()) {
                    if (pageBody1.getPage() + 2 > pageBody1.getPages()) {
                        for (int i = pageBody1.getPages() - 5; i < pageBody1.getPages(); i++) {
                            pageList.add(i + 1);
                        }
                    } else {
                        pageList.add(pageBody1.getPage() - 2);
                        pageList.add(pageBody1.getPage() - 1);
                        pageList.add(pageBody1.getPage());
                        pageList.add(pageBody1.getPage() + 1);
                        pageList.add(pageBody1.getPage() + 2);
                    }
                } else {
                    pageBody1.setPage(pageBody1.getPages());
                    for (int i = pageBody1.getPages() - 5; i < pageBody1.getPages(); i++) {
                        pageList.add(i + 1);
                    }
                }
            } else {
                if (reserveList.size() % 5 != 0) {
                    pageBody1.setPages((reserveList.size() + 5) / 5);
                } else {
                    pageBody1.setPages(reserveList.size() / 5);
                }

                if (pageBody1.getPage() <= pageBody1.getPages()) {
                    for (int i = 0; i < pageBody1.getPages(); i++) {
                        pageList.add(i + 1);
                    }
                } else {
                    pageBody1.setPage(pageBody1.getPages());
                    for (int i = 0; i < pageBody1.getPages(); i++) {
                        pageList.add(i + 1);
                    }
                }
            }

            reserveList = reserveList.subList(pageBody1.getPage() * 5 - 5, pageBody1.getPage() * 5 > reserveList.size() ? reserveList.size() : pageBody1.getPage() * 5);
            pageBody1.setPageList(pageList);
        }

        return Map.of("reserveList", reserveList, "pageBody1", pageBody1);
    }

    @GetMapping("/deleteReserve/{no}")
    // 取消订单
    public Map deleteReserve(@PathVariable String no,
                             @RequestAttribute String username) {
        //System.out.println("get success!" + no + username);

        String message = "";

        Reserve reserve = reserveService.findByNo(no);
        if (reserve != null) {
            reserveService.deleteReserve(reserve);
            message = "删除成功！";
        }

        return Map.of("message", message);
    }

    @GetMapping("getOrdering/{no}")
    // 获得点餐信息
    public Map getOrdering(@PathVariable String no) {
        //System.out.println("get success!" + no);

        List<Ordering> orderingList = new ArrayList<Ordering>();
        orderingList = orderingService.findByReserveNo(no);

        return Map.of("orderingList", orderingList);
    }

    @GetMapping("settleAccounts/{no}")
    // 结账
    public Map settleAccounts(@PathVariable String no) {
        //System.out.println("get success!" + no);

        String message = "";
        Reserve reserve = new Reserve();
        List<Ordering> orderingList = new ArrayList<Ordering>();
        OrderingComplete orderingComplete = new OrderingComplete();

        reserve = reserveService.findByNo(no);
        orderingList = orderingService.findByReserveNo(no);

        double totalPrice = 0.0;
        for (Ordering ordering : orderingList) {
            BigDecimal count = new BigDecimal(Double.toString(ordering.getCount()));
            BigDecimal price = new BigDecimal(Double.toString(ordering.getMenu().getPrice()));
            BigDecimal oldTotalPrice = new BigDecimal(Double.toString(totalPrice));
            totalPrice = count.multiply(price).setScale(2, BigDecimal.ROUND_HALF_UP).add(oldTotalPrice).doubleValue();
        }

        orderingComplete.setTotalPrice(totalPrice);
        orderingComplete.setReserveNo(no);
        orderingComplete.setCustomer(reserve.getCustomer());
        orderingCompleteService.save(orderingComplete);

        reserveService.deleteReserve(reserve);
        message = "已结算！";

        return Map.of("message", message);
    }

    @PostMapping("/submitRepair")
    // 提交报修报损信息
    public Map submitRepair(@RequestBody Repair repair) {
        //System.out.println("post success!" + repair.getContent() + repair.getCause());

        repair.setState(Repair.state1);
        repair.setPrice(0.0);
        repairService.save(repair);

        Repair repair1 = new Repair();
        repair1.setRepairType(Repair.repairType1);
        repair1.setContent("");
        repair1.setCause("");

        return Map.of("message", "提交成功！", "repair", repair1);
    }

    @PostMapping("changePassword")
    // 修改密码
    public Map changePassword(@RequestBody UserBodyChangePassword userBodyChangePassword) {
        //System.out.println("post success!" + userBody1.getUsername());

        String message = "";
        Staff staff = new Staff();

        staff = staffService.findByUsername(userBodyChangePassword.getUsername());
        if (!passwordEncoder.matches(userBodyChangePassword.getPassword(), staff.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "旧密码错误！");
        } else {
            staff.setPassword(passwordEncoder.encode(userBodyChangePassword.getNewPassword()));
            staffService.updateStaff(staff);
            message = "修改成功！";
        }

        return Map.of("message", message);
    }

    @GetMapping("getRepair")
    // 获得未完成报修报损信息
    public Map getRepair() {
        //System.out.println("get success!");

        int page = 1;
        int pages = 0;
        List<Integer> pageList = new ArrayList<Integer>();

        List<Repair> repairList = new ArrayList<Repair>();
        repairList = repairService.findByState(Repair.state1);

        if (repairList.size() / 5 >= 5) {
            for (int i = 0; i < 5; i++) {
                pageList.add(i + 1);
            }

            if (repairList.size() % 5 != 0) {
                pages = (repairList.size() + 5) / 5;
            } else {
                pages = repairList.size() / 5;
            }
            repairList = repairList.subList(0, 5);
        } else {
            if (!repairList.isEmpty()) {
                if (repairList.size() % 5 != 0) {
                    pages = (repairList.size() + 5) / 5;
                } else {
                    pages = repairList.size() / 5;
                }

                for (int i = 0; i < pages; i++) {
                    pageList.add(i + 1);
                }

                if (repairList.size() < 5) {
                    repairList = repairList.subList(0, repairList.size());
                } else {
                    repairList = repairList.subList(0, 5);
                }
            }
        }

        PageBody1 pageBody1 = new PageBody1();
        pageBody1.setPage(page);
        pageBody1.setPages(pages);
        pageBody1.setPageList(pageList);

        return Map.of("repairList", repairList, "pageBody1", pageBody1);
    }

    @PostMapping("/doPage1")
    // 报修报损分页
    public Map doPage1(@RequestBody PageBody1 pageBody1) {
        //System.out.println("post success!" + pageBody1.getPage());

        List<Integer> pageList = new ArrayList<Integer>();
        List<Repair> repairList = new ArrayList<Repair>();

        repairList = repairService.findByState(Repair.state1);
        if (repairList.isEmpty()) {
            pageBody1.setPage(0);
            pageBody1.setPages(0);
            pageBody1.setPageList(pageList);
        } else {
            if ((double) repairList.size() / 5 > 5.0) {
                if (repairList.size() % 5 != 0) {
                    pageBody1.setPages((repairList.size() + 5) / 5);
                } else {
                    pageBody1.setPages(repairList.size() / 5);
                }

                if (pageBody1.getPage() <= pageBody1.getPages()) {
                    if (pageBody1.getPage() + 2 > pageBody1.getPages()) {
                        for (int i = pageBody1.getPages() - 5; i < pageBody1.getPages(); i++) {
                            pageList.add(i + 1);
                        }
                    } else {
                        pageList.add(pageBody1.getPage() - 2);
                        pageList.add(pageBody1.getPage() - 1);
                        pageList.add(pageBody1.getPage());
                        pageList.add(pageBody1.getPage() + 1);
                        pageList.add(pageBody1.getPage() + 2);
                    }
                } else {
                    pageBody1.setPage(pageBody1.getPages());
                    for (int i = pageBody1.getPages() - 5; i < pageBody1.getPages(); i++) {
                        pageList.add(i + 1);
                    }
                }
            } else {
                if (repairList.size() % 5 != 0) {
                    pageBody1.setPages((repairList.size() + 5) / 5);
                } else {
                    pageBody1.setPages(repairList.size() / 5);
                }

                if (pageBody1.getPage() <= pageBody1.getPages()) {
                    for (int i = 0; i < pageBody1.getPages(); i++) {
                        pageList.add(i + 1);
                    }
                } else {
                    pageBody1.setPage(pageBody1.getPages());
                    for (int i = 0; i < pageBody1.getPages(); i++) {
                        pageList.add(i + 1);
                    }
                }
            }

            repairList = repairList.subList(pageBody1.getPage() * 5 - 5, pageBody1.getPage() * 5 > repairList.size() ? repairList.size() : pageBody1.getPage() * 5);
            pageBody1.setPageList(pageList);
        }

        return Map.of("repairList", repairList, "pageBody1", pageBody1);
    }

    @PostMapping("deleteRepair")
    // 删除报修报损信息
    public Map deleteRepair(@RequestBody Repair repair) {
        //System.out.println("post success!");

        String message = "";
        repairService.deleteRepair(repair);
        message = "删除成功！";

        return Map.of("message", message);
    }

    @PostMapping("completeRepair")
    // 完成报修报损
    public Map completeRepair(@RequestBody Repair repair) {
        //System.out.println("post success!" + repair.getPrice());

        repair.setState(Repair.state2);
        repairService.save(repair);

        return Map.of("message", "已完成！");
    }

    @PostMapping("addRestaurantMaterial")
    public Map addRestaurantMaterial(@RequestBody RestaurantMaterial restaurantMaterial) {
        if (restaurantMaterialService.findByName(restaurantMaterial.getName()) != null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "该餐厅物资已存在！");
        }
        restaurantMaterialService.save(restaurantMaterial);

        return Map.of("message", "添加成功！");
    }

    @GetMapping("initRestaurantMaterial")
    public Map initRestaurantMaterial() {
        int page = 1;
        int pages = 0;
        List<Integer> pageList = new ArrayList<Integer>();
        List<RestaurantMaterial> restaurantMaterialList = new ArrayList<RestaurantMaterial>();

        restaurantMaterialList = restaurantMaterialService.findAll();
        if (restaurantMaterialList.size() / 5 >= 5) {
            for (int i = 0; i < 5; i++) {
                pageList.add(i + 1);
            }

            if (restaurantMaterialList.size() % 5 != 0) {
                pages = (restaurantMaterialList.size() + 5) / 5;
            } else {
                pages = restaurantMaterialList.size() / 5;
            }
            restaurantMaterialList = restaurantMaterialList.subList(0, 5);
        } else {
            if (!restaurantMaterialList.isEmpty()) {
                if (restaurantMaterialList.size() % 5 != 0) {
                    pages = (restaurantMaterialList.size() + 5) / 5;
                } else {
                    pages = restaurantMaterialList.size() / 5;
                }

                for (int i = 0; i < pages; i++) {
                    pageList.add(i + 1);
                }

                if (restaurantMaterialList.size() < 5) {
                    restaurantMaterialList = restaurantMaterialList.subList(0, restaurantMaterialList.size());
                } else {
                    restaurantMaterialList = restaurantMaterialList.subList(0, 5);
                }
            }
        }

        PageBody1 pageBody1 = new PageBody1();
        pageBody1.setPage(page);
        pageBody1.setPages(pages);
        pageBody1.setPageList(pageList);

        return Map.of("restaurantMaterialList", restaurantMaterialList, "pageBody1", pageBody1);
    }

    @PostMapping("/doPage2")
    public Map doPage2(@RequestBody PageBody1 pageBody1) {
        List<Integer> pageList = new ArrayList<Integer>();
        List<RestaurantMaterial> restaurantMaterialList = new ArrayList<RestaurantMaterial>();

        restaurantMaterialList = restaurantMaterialService.findAll();
        if (restaurantMaterialList.isEmpty()) {
            pageBody1.setPage(0);
            pageBody1.setPages(0);
            pageBody1.setPageList(pageList);
        } else {
            if ((double) restaurantMaterialList.size() / 5 > 5.0) {
                if (restaurantMaterialList.size() % 5 != 0) {
                    pageBody1.setPages((restaurantMaterialList.size() + 5) / 5);
                } else {
                    pageBody1.setPages(restaurantMaterialList.size() / 5);
                }

                if (pageBody1.getPage() <= pageBody1.getPages()) {
                    if (pageBody1.getPage() + 2 > pageBody1.getPages()) {
                        for (int i = pageBody1.getPages() - 5; i < pageBody1.getPages(); i++) {
                            pageList.add(i + 1);
                        }
                    } else {
                        pageList.add(pageBody1.getPage() - 2);
                        pageList.add(pageBody1.getPage() - 1);
                        pageList.add(pageBody1.getPage());
                        pageList.add(pageBody1.getPage() + 1);
                        pageList.add(pageBody1.getPage() + 2);
                    }
                } else {
                    pageBody1.setPage(pageBody1.getPages());
                    for (int i = pageBody1.getPages() - 5; i < pageBody1.getPages(); i++) {
                        pageList.add(i + 1);
                    }
                }
            } else {
                if (restaurantMaterialList.size() % 5 != 0) {
                    pageBody1.setPages((restaurantMaterialList.size() + 5) / 5);
                } else {
                    pageBody1.setPages(restaurantMaterialList.size() / 5);
                }

                if (pageBody1.getPage() <= pageBody1.getPages()) {
                    for (int i = 0; i < pageBody1.getPages(); i++) {
                        pageList.add(i + 1);
                    }
                } else {
                    pageBody1.setPage(pageBody1.getPages());
                    for (int i = 0; i < pageBody1.getPages(); i++) {
                        pageList.add(i + 1);
                    }
                }
            }

            restaurantMaterialList = restaurantMaterialList.subList(pageBody1.getPage() * 5 - 5, pageBody1.getPage() * 5 > restaurantMaterialList.size() ? restaurantMaterialList.size() : pageBody1.getPage() * 5);
            pageBody1.setPageList(pageList);
        }

        return Map.of("restaurantMaterialList", restaurantMaterialList, "pageBody1", pageBody1);
    }

    @PostMapping("addRestaurantMaterialPurchase")
    public Map addRestaurantMaterialPurchase(@RequestBody Purchase purchase) {
        RestaurantMaterial restaurantMaterial = restaurantMaterialService.findByName(purchase.getRestaurantMaterial().getName());
        BigDecimal bigDecimal = new BigDecimal(Double.toString(purchase.getAmount()));
        BigDecimal bigDecimal1 = new BigDecimal(Double.toString(restaurantMaterial.getAmount()));
        restaurantMaterial.setAmount(bigDecimal1.add(bigDecimal).doubleValue());
        restaurantMaterialService.save(restaurantMaterial);

        Purchase purchase1 = new Purchase();
        purchase1.setAmount(purchase.getAmount());
        purchase1.setPrice(purchase.getPrice());
        purchase1.setRestaurantMaterial(purchase.getRestaurantMaterial());
        purchaseService.save(purchase1);

        return Map.of("message", "提交成功！");
    }

    @PostMapping("deleteRestaurantMaterial")
    public Map deleteRestaurantMaterial(@RequestBody RestaurantMaterial restaurantMaterial) {
        RestaurantMaterial restaurantMaterial1 = restaurantMaterialService.findByName(restaurantMaterial.getName());
        restaurantMaterialService.delete(restaurantMaterial1);

        return Map.of("message", "删除成功！");
    }

    @PostMapping("consumptionRestaurantMaterial")
    public Map consumptionRestaurantMaterial(@RequestBody RestaurantMaterial restaurantMaterial) {
        RestaurantMaterial restaurantMaterial1 = restaurantMaterialService.findByName(restaurantMaterial.getName());
        BigDecimal bigDecimal = new BigDecimal(Double.toString(restaurantMaterial.getAmount()));
        BigDecimal bigDecimal1 = new BigDecimal(Double.toString(restaurantMaterial1.getAmount()));
        restaurantMaterial1.setAmount(bigDecimal1.subtract(bigDecimal).doubleValue());
        restaurantMaterialService.save(restaurantMaterial1);

        return Map.of("message", "已提交！");
    }

    @PostMapping("useRestaurantMaterial")
    public Map useRestaurantMaterial(@RequestBody RestaurantMaterial restaurantMaterial) {
        RestaurantMaterial restaurantMaterial1 = restaurantMaterialService.findByName(restaurantMaterial.getName());
        BigDecimal bigDecimal = new BigDecimal(Double.toString(restaurantMaterial.getAmount()));
        BigDecimal bigDecimal1 = new BigDecimal(Double.toString(restaurantMaterial1.getAmount()));
        restaurantMaterial1.setAmount(bigDecimal1.subtract(bigDecimal).doubleValue());
        restaurantMaterialService.save(restaurantMaterial1);

        return Map.of("message", "已提交！");
    }
}
