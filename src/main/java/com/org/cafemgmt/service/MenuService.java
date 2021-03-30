package com.org.cafemgmt.service;

import com.org.cafemgmt.model.CafeMenus;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface MenuService {
    public List<CafeMenus> listAllMenus();

    public boolean activateMenu(long id);

    public boolean deactivateMenu(long id);

    public CafeMenus saveMenu(CafeMenus cafeMenus);

    public Optional<CafeMenus> getMenuById(long id);

    public void deleteMenuById(long id);

    public List<CafeMenus> listAllActiveMenus();

    public CafeMenus updateMenu(CafeMenus cafeMenus, long id);
}
