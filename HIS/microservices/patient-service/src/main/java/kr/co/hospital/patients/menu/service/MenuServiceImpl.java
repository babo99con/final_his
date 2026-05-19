package kr.co.hospital.patients.menu.service;

import kr.co.hospital.patients.menu.dto.MenuFlatDto;
import kr.co.hospital.patients.menu.dto.MenuTreeRes;
import kr.co.hospital.patients.menu.mapper.MenuMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuServiceImpl implements MenuService {

    private final MenuMapper menuMapper;

    @Override
    public List<MenuTreeRes> getMenus() {
        List<MenuFlatDto> flatList = menuMapper.findHierarchy();
        return buildTreeFromFlat(flatList);
    }

    private List<MenuTreeRes> buildTreeFromFlat(List<MenuFlatDto> flatList) {
        Map<Long, MenuTreeRes> nodeMap = new LinkedHashMap<>();
        List<MenuTreeRes> roots = new ArrayList<>();

        for (MenuFlatDto dto : flatList) {
            if (dto == null) continue;

            MenuTreeRes node = new MenuTreeRes();
            node.setId(dto.getId());
            node.setCode(dto.getCode());
            node.setName(dto.getName());
            node.setPath(dto.getPath());
            node.setIcon(dto.getIcon());
            node.setSortOrder(dto.getSortOrder());
            nodeMap.put(dto.getId(), node);

            if (dto.getParentId() == null) {
                roots.add(node);
            } else {
                MenuTreeRes parent = nodeMap.get(dto.getParentId());
                if (parent != null) {
                    parent.getChildren().add(node);
                } else {
                    roots.add(node);
                }
            }
        }

        pruneEmptyGroups(roots);
        return roots;
    }

    private void pruneEmptyGroups(List<MenuTreeRes> nodes) {
        if (nodes == null) return;
        nodes.removeIf(node -> {
            List<MenuTreeRes> children = node.getChildren();
            if (children != null && !children.isEmpty()) {
                pruneEmptyGroups(children);
            }
            boolean hasChildren = children != null && !children.isEmpty();
            boolean hasPath = node.getPath() != null && !node.getPath().isBlank();
            return !hasChildren && !hasPath;
        });
    }
}
