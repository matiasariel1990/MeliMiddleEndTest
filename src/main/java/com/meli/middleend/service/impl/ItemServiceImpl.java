package com.meli.middleend.service.impl;

import com.meli.middleend.client.ApiMLClient;
import com.meli.middleend.dto.Author;
import com.meli.middleend.dto.Item;
import com.meli.middleend.dto.ItemDeteail;
import com.meli.middleend.dto.QueryDto;
import com.meli.middleend.dto.api.client.SearByQueryDto;
import com.meli.middleend.dto.api.client.response.ItemByIdResponse;
import com.meli.middleend.dto.api.client.response.ItemDescription;
import com.meli.middleend.dto.api.client.response.SearchResponse;
import com.meli.middleend.dto.response.ItemResponse;
import com.meli.middleend.dto.response.PageItemResponse;
import com.meli.middleend.service.ItemService;
import com.meli.middleend.utils.Mapper;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

import static com.meli.middleend.utils.StringConstants.NO_AVAILABLE_DATA;

@Service
@Qualifier("ItemServiceImpl")
@Setter
public class ItemServiceImpl implements ItemService {


    @Autowired
    @Qualifier("StateApiHealth")
    ApiMLClient apiMLClient;

    @Override
    public ItemResponse getItemById(String id) {
        ItemByIdResponse itemByIdResponse = apiMLClient.getItemById(id);
        ItemDeteail itemDeteail = Mapper.mapToItemDetail(itemByIdResponse);
        try{
            ItemDescription itemDescription = apiMLClient.getItemDescription(id);
            itemDeteail.setDescription(itemDescription.getPlain_text());
        }catch (Exception e){
            itemDeteail.setDescription(NO_AVAILABLE_DATA);
        }
        Author author = Author.builder()
                .name(itemByIdResponse.getSeller_id())
                .lastName(NO_AVAILABLE_DATA).build();
        ItemResponse itemResponse = ItemResponse.builder().author(author)
                .item(itemDeteail).build();
        return itemResponse;
    }

    @Override
    public PageItemResponse getItemsByQuery(QueryDto query) {

        SearByQueryDto searByQueryDto = Mapper.mapToSearByQueryDto(query);

        SearchResponse searchResponse = apiMLClient.searchByQuery(searByQueryDto);

        PageItemResponse pageItemResponse = PageItemResponse.builder().build();
        pageItemResponse.setPaging(searchResponse.getPaging());
        HashSet<String> categories = new HashSet<>();
        List<Item> itemList = searchResponse.getResults()
                .stream().peek(item -> categories.add(item.getCategory_id())).map(Mapper::mapToItem).toList();
        pageItemResponse.setItems(itemList);
        pageItemResponse.setCategories(categories);

        return pageItemResponse;
    }
}
