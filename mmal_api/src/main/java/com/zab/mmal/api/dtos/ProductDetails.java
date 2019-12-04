package com.zab.mmal.api.dtos;

import com.zab.mmal.api.entity.MmallProduct;
import lombok.Data;

@Data
public class ProductDetails extends MmallProduct {
    private static final long serialVersionUID = 548844884584226161L;
    private String imagesHost;
    private Integer parentCategoryId;
}
