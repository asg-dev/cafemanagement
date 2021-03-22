package com.org.cafemgmt.service;

import com.org.cafemgmt.model.CafeMenuItems;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface MenuItemsService {
    public List<CafeMenuItems> getMenuItemByMenuId(long id);
    public List<CafeMenuItems> findAllMenuItemsById(List<Long> ids);
    public List<CafeMenuItems> getAllMenuItems();
    public void saveMenuItem(CafeMenuItems cafeMenuItems);
    public String saveMenuItemImage(MultipartFile file);
    public void deleteMenuItem(long id);
 }
