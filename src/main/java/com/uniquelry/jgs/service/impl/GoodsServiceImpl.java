package com.uniquelry.jgs.service.impl;

import com.uniquelry.jgs.entity.Goods;
import com.uniquelry.jgs.repository.GoodsRepository;
import com.uniquelry.jgs.service.GoodsService;
import com.uniquelry.jgs.util.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * @Description  商品Service实现类
 * @Author uniquelry
 * @Email 1909506001@qq.com
 * @Date 2018/10/20 0:23
 * @Version V1.0
 */
@Service("goodsService")
public class GoodsServiceImpl implements GoodsService {

	@Resource
	private GoodsRepository goodsRepository;
	
	@Override
	public Goods findById(Integer id) {
		return goodsRepository.findOne(id);
	}

	@Override
	public void save(Goods goods) {
		goodsRepository.save(goods);
	}

	@Override
	public List<Goods> list(Goods goods, Integer page, Integer pageSize, Direction direction, String... properties) {
		Pageable pageable=new PageRequest(page-1, pageSize, direction,properties);
		Page<Goods> pageUser=goodsRepository.findAll(new Specification<Goods>() {
			
			@Override
			public Predicate toPredicate(Root<Goods> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate=cb.conjunction();
				if(goods!=null){
					if(StringUtils.isNotEmpty(goods.getName())){
						predicate.getExpressions().add(cb.like(root.get("name"), "%"+goods.getName().trim()+"%"));
					}	
					if(goods.getType()!=null && goods.getType().getId()!=null && goods.getType().getId()!=1){
						predicate.getExpressions().add(cb.equal(root.get("type").get("id"), goods.getType().getId()));
					}
					if(StringUtils.isNotEmpty(goods.getCodeOrName())){
						predicate.getExpressions().add(cb.or(cb.like(root.get("code"),"%"+goods.getCodeOrName()+"%"), cb.like(root.get("name"),"%"+goods.getCodeOrName()+"%")));
					}
				}
				return predicate;
			}
		}, pageable);
		return pageUser.getContent();
	}

	@Override
	public Long getCount(Goods goods) {
		Long count=goodsRepository.count(new Specification<Goods>() {

			@Override
			public Predicate toPredicate(Root<Goods> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate=cb.conjunction();
				if(goods!=null){
					if(StringUtils.isNotEmpty(goods.getName())){
						predicate.getExpressions().add(cb.like(root.get("name"), "%"+goods.getName().trim()+"%"));
					}	
					if(goods.getType()!=null && goods.getType().getId()!=null && goods.getType().getId()!=1){
						predicate.getExpressions().add(cb.equal(root.get("type").get("id"), goods.getType().getId()));
					}
					if(StringUtils.isNotEmpty(goods.getCodeOrName())){
						predicate.getExpressions().add(cb.or(cb.like(root.get("code"),"%"+goods.getCodeOrName()+"%"), cb.like(root.get("name"),"%"+goods.getCodeOrName()+"%")));
					}
				}
				return predicate;
			}
		});
		return count;
	}

	@Override
	public void delete(Integer id) {
		goodsRepository.delete(id);
	}

	@Override
	public String getMaxGoodsCode() {
		return goodsRepository.getMaxGoodsCode();
	}

	@Override
	public List<Goods> listNoInventoryQuantityByCodeOrName(String codeOrName, Integer page, Integer pageSize,
			Direction direction, String... properties) {
		Pageable pageable=new PageRequest(page-1, pageSize, direction,properties);
		Page<Goods> pageUser=goodsRepository.findAll(new Specification<Goods>() {
			
			@Override
			public Predicate toPredicate(Root<Goods> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate=cb.conjunction();
				if(StringUtils.isNotEmpty(codeOrName)){
					predicate.getExpressions().add(cb.or(cb.like(root.get("code"),"%"+codeOrName+"%"), cb.like(root.get("name"),"%"+codeOrName+"%")));
				}
				predicate.getExpressions().add(cb.equal(root.get("inventoryQuantity"), 0)); // 库存是0
				return predicate;
			}
		}, pageable);
		return pageUser.getContent();
	}

	@Override
	public Long getCountNoInventoryQuantityByCodeOrName(String codeOrName) {
		Long count=goodsRepository.count(new Specification<Goods>() {

			@Override
			public Predicate toPredicate(Root<Goods> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate=cb.conjunction();
				if(StringUtils.isNotEmpty(codeOrName)){
					predicate.getExpressions().add(cb.or(cb.like(root.get("code"),"%"+codeOrName+"%"), cb.like(root.get("name"),"%"+codeOrName+"%")));
				}
				predicate.getExpressions().add(cb.equal(root.get("inventoryQuantity"), 0)); // 库存是0
				return predicate;
			}
		});
		return count;
	}

	@Override
	public List<Goods> listHasInventoryQuantity(Integer page, Integer pageSize, Direction direction,
			String... properties) {
		Pageable pageable=new PageRequest(page-1, pageSize, direction,properties);
		Page<Goods> pageUser=goodsRepository.findAll(new Specification<Goods>() {
			
			@Override
			public Predicate toPredicate(Root<Goods> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate=cb.conjunction();
				predicate.getExpressions().add(cb.greaterThan(root.get("inventoryQuantity"), 0)); // 库存不是0
				return predicate;
			}
		}, pageable);
		return pageUser.getContent();
	}

	@Override
	public Long getCountHasInventoryQuantity() {
		Long count=goodsRepository.count(new Specification<Goods>() {

			@Override
			public Predicate toPredicate(Root<Goods> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate=cb.conjunction();
				predicate.getExpressions().add(cb.greaterThan(root.get("inventoryQuantity"), 0)); // 库存不是0
				return predicate;
			}
		});
		return count;
	}

	@Override
	public List<Goods> listAlarm() {
		return goodsRepository.listAlarm();
	}



}
