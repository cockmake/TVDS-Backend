package com.tvds.newtvdsbackend.utils;

import com.tvds.newtvdsbackend.configuration.MinioConfig;
import com.tvds.newtvdsbackend.domain.vo.VisualPromptVO;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@ConfigurationProperties(prefix = "common")
@Data
public class CommonUtil {
    private List<String> imageType;


    private static List<String> staticImageType;

    @PostConstruct
    public void init() {
        // 这里可以进行一些初始化操作
        // 比如将 imageType 和 staticImageType 转换为小写
        staticImageType = imageType;
    }

    static public boolean imageTypeCheck(String extension) {
        return staticImageType.contains(extension);
    }

    static public List<String> imageTypeList() {
        return staticImageType;
    }


    static public Map<String, Map<String, Object>> formatVisualPrompt(List<VisualPromptVO> visualPromptList, String bucketName) {
        // 其中VisualPromptVO(
        // componentId=1915454965191065601, componentName=123, componentType=默认,
        // imagePath=1915454965191065601/1915788577791279104.jpg, x1=184.0, y1=183.0, x2=275.0, y2=238.0,
        // detectionConf=0.0, detectionIou=0.0, abnormalityDesc=''
        // )
        // 以componentId为key
        // 信息包括componentName, componentType
        // 列表 imagePath x1, y1, x2, y2
        // {
        //     "1915454965191065601": {
        //         "componentName": "123",
        //         "componentType": "默认",
        //         "detectionConf": 0.0,
        //         "detectionIou": 0.0,
        //         "abnormalityDesc": "",
        //         "templateImage": [
        //             {
        //                 "imagePath": "1915454965191065601/1915788577791279104.jpg",
        //                 'boxes': [[x1, y1, x2, y2], [x1, y1, x2, y2]]
        //             }
        //         ]
        //     }
        // }
        return visualPromptList.stream().collect(
                Collectors.groupingBy(
                        VisualPromptVO::getComponentId, // 按 componentId 分组
                        Collectors.collectingAndThen(   // 对每个 componentId 分组的 List<VisualPromptVO> 进行后续处理
                                Collectors.toList(),        // 先将分组结果收集成 List
                                list -> {                   // 处理这个 List
                                    if (list.isEmpty()) {
                                        return new HashMap<String, Object>(); // 处理空列表情况
                                    }
                                    // 从列表的第一个元素获取 componentName 和 componentType
                                    VisualPromptVO firstVO = list.get(0);
                                    Map<String, Object> componentInfo = new HashMap<>();
                                    componentInfo.put("componentName", firstVO.getComponentName());
                                    componentInfo.put("componentType", firstVO.getComponentType());
                                    componentInfo.put("detectionConf", firstVO.getDetectionConf());
                                    componentInfo.put("detectionIou", firstVO.getDetectionIou());
                                    componentInfo.put("abnormalityDesc", firstVO.getAbnormalityDesc());
                                    componentInfo.put("bucketName", bucketName);

                                    // 按 imagePath 对当前 componentId 的 VisualPromptVO 列表进行分组
                                    Map<String, List<VisualPromptVO>> imagesGroupedByPath = list.stream()
                                            .collect(Collectors.groupingBy(VisualPromptVO::getImagePath));

                                    // 构建 templateImage 列表
                                    List<Map<String, Object>> templateImageList = imagesGroupedByPath.entrySet().stream()
                                            .map(entry -> {
                                                String imagePath = entry.getKey();
                                                List<VisualPromptVO> vosForPath = entry.getValue();

                                                // 提取当前 imagePath 对应的所有 boxes
                                                List<List<Float>> boxes = vosForPath.stream()
                                                        .map(vo -> Arrays.asList(vo.getX1(), vo.getY1(), vo.getX2(), vo.getY2()))
                                                        .collect(Collectors.toList());

                                                Map<String, Object> imageDetails = new HashMap<>();
                                                imageDetails.put("imagePath", imagePath);
                                                imageDetails.put("boxes", boxes);
                                                return imageDetails;
                                            })
                                            .collect(Collectors.toList());

                                    componentInfo.put("templateImage", templateImageList);
                                    return componentInfo; // 返回构建好的内部 Map
                                }
                        )
                ));
    }
}
