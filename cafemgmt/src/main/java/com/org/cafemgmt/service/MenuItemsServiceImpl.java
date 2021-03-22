package com.org.cafemgmt.service;

import com.org.cafemgmt.model.CafeMenuItems;
import com.org.cafemgmt.repository.MenuItemsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

public class MenuItemsServiceImpl implements MenuItemsService {
    @Autowired
    MenuItemsRepository menuItemsRepository;

    private final Path menuItemRoot = Paths.get("uploads/menu_items");

    @Override
    public List<CafeMenuItems> getMenuItemByMenuId(long id) {
        return menuItemsRepository.findMenuItemsByMenuId(id);
    }

    @Override
    public List<CafeMenuItems> findAllMenuItemsById(List<Long> ids) {
        return menuItemsRepository.findAllById(ids);
    }

    @Override
    public List<CafeMenuItems> getAllMenuItems() {
        return menuItemsRepository.findAll();
    }

    @Override
    public void saveMenuItem(CafeMenuItems cafeMenuItems) {
        cafeMenuItems.setCreatedAt(new Date());
        cafeMenuItems.setUpdatedAt(new Date());
        menuItemsRepository.save(cafeMenuItems);
    }

    @Override
    public String saveMenuItemImage(MultipartFile file) {
        try {
            Files.copy(file.getInputStream(), this.menuItemRoot.resolve(file.getOriginalFilename()));
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
        return (menuItemRoot.toString() + File.separator + file.getOriginalFilename());
    }

    @Override
    public void deleteMenuItem(long id) {
        menuItemsRepository.deleteById(id);
    }

}
