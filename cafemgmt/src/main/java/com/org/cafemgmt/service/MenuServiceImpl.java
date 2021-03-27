package com.org.cafemgmt.service;

import com.org.cafemgmt.model.CafeMenus;
import com.org.cafemgmt.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public class MenuServiceImpl implements MenuService {

    @Autowired
    MenuRepository menuRepository;

    @Override
    public List<CafeMenus> listAllMenus() {

        return menuRepository.findAll();
    }

    @Override
    public boolean activateMenu(long id) {
        Optional<CafeMenus> cafeMenu = menuRepository.findById(id);
        boolean flag = false;
        if (cafeMenu.isPresent()) {
            CafeMenus existingMenu = cafeMenu.get();
            existingMenu.setActive(true);
            flag = true;
            menuRepository.save(existingMenu);
        }
        return flag;
    }

    @Override
    public boolean deactivateMenu(long id) {
        Optional<CafeMenus> cafeMenu = menuRepository.findById(id);
        boolean flag = false;
        if (cafeMenu.isPresent()) {
            CafeMenus existingMenu = cafeMenu.get();
            existingMenu.setActive(false);
            flag = true;
            menuRepository.save(existingMenu);
        }
        return flag;
    }

    @Override
    public CafeMenus saveMenu(CafeMenus cafeMenus) {
        if (cafeMenus.getCreated_at() == null) {
            cafeMenus.setCreated_at(new Date());
        }
        cafeMenus.setUpdated_at(new Date());
        System.out.println(cafeMenus.getMenu_items());
        return menuRepository.save(cafeMenus);
    }

    @Override
    public Optional<CafeMenus> getMenuById(long id) {
        return menuRepository.findById(id);
    }

    @Override
    public void deleteMenuById(long id) {
        menuRepository.deleteById(id);
    }

    @Override
    public List<CafeMenus> listAllActiveMenus() {
        return menuRepository.listAllActiveMenus();
    }

    @Override
    public CafeMenus updateMenu(CafeMenus cafeMenus, long id) {
        Optional<CafeMenus> existingMenuOptional = menuRepository.findById(id);
        if (existingMenuOptional.isPresent()) {
            CafeMenus existingMenu = existingMenuOptional.get();
            existingMenu.setUpdated_at(new Date());
            existingMenu.setName(cafeMenus.getName());
            existingMenu.setDescription(cafeMenus.getDescription());
            existingMenu.setMenu_items(cafeMenus.getMenu_items());
            return menuRepository.save(existingMenu);
        }
        return null;
    }

}
