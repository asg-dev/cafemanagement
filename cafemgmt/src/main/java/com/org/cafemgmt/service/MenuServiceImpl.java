package com.org.cafemgmt.service;

import com.org.cafemgmt.model.CafeMenuItems;
import com.org.cafemgmt.model.CafeMenus;
import com.org.cafemgmt.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    private final Path root = Paths.get("uploads/menus");
    private final Path dbRoot = Paths.get("/images/uploads/menus");

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
    public String saveMenuImage(MultipartFile file) {
        try {
            Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage() + e.getLocalizedMessage());
        }
        return (root.toString() + File.separator + file.getOriginalFilename());
    }

    @Override
    public void saveMenu(CafeMenus cafeMenus) {
        if (cafeMenus.getCreated_at() == null) {
            cafeMenus.setCreated_at(new Date());
        }
        cafeMenus.setUpdated_at(new Date());
        System.out.println(cafeMenus.getMenu_items());
        menuRepository.save(cafeMenus);
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

}
