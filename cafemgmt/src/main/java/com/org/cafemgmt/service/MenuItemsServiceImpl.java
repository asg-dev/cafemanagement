package com.org.cafemgmt.service;

import com.org.cafemgmt.model.CafeMenuItems;
import com.org.cafemgmt.repository.MenuItemsRepository;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class MenuItemsServiceImpl implements MenuItemsService {
    @Autowired
    MenuItemsRepository menuItemsRepository;

    private final Path menuItemRoot = Paths.get("uploads/menu_items");

    @Override
    public List<CafeMenuItems> getMenuItemByMenuId(long id) {
        return menuItemsRepository.findMenuItemsByMenuId(id);
    }

    @Override
    public CafeMenuItems getMenuItemById(long id) {
        Optional<CafeMenuItems> cafeMenuItem = menuItemsRepository.findById(id);
        if (cafeMenuItem.isPresent()) {
            return cafeMenuItem.get();
        }
        return null;
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
    public CafeMenuItems saveMenuItem(CafeMenuItems cafeMenuItems) {
        cafeMenuItems.setCreatedAt(new Date());
        cafeMenuItems.setUpdatedAt(new Date());
        return menuItemsRepository.save(cafeMenuItems);
    }

    @Override
    public String saveMenuItemImage(MultipartFile file) {
        try {
            String[] extensionSplit = file.getOriginalFilename().split("\\.");
            String extension = extensionSplit[extensionSplit.length - 1];
            if (extension.equals("jpg") || extension.equals("jpeg") || extension.equals("png") || extension.equals("gif") || extension.equals("bmp")) {
                String filename = file.getOriginalFilename().replace('.' + extension, "") + RandomString.make(6) + '.' + extension;
                Files.copy(file.getInputStream(), this.menuItemRoot.resolve(filename));
                return (menuItemRoot.toString() + File.separator + filename);
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    @Override
    public void deleteMenuItem(long id) {
        menuItemsRepository.deleteById(id);
    }

    @Override
    public boolean checkMenuItemValidity(List<Long> incomingMenuItems) {
        boolean isMenuValid = false;
        if (checkForDuplicates(incomingMenuItems)) {
            return false;
        }
        for (Long menuItem : incomingMenuItems) {
            Optional<CafeMenuItems> menuItems = menuItemsRepository.findById(menuItem);
            if (menuItems.isPresent()) {
                isMenuValid = true;
            } else {
                isMenuValid = false;
                break;
            }
        }
        return isMenuValid;
    }

    private boolean checkForDuplicates(List<Long> incomingList) {
        Set<Long> longSet = new HashSet<Long>(incomingList);
        if (longSet.size() < incomingList.size()) {
            return true;
        }
        return false;
    }

}
