package com.altqart.services;
import java.util.List;

import com.altqart.model.WithdrawCat;

public interface WithdrawCatServices {

	public List<WithdrawCat> getAllWithDarwCats();

	public long getCount();

	public WithdrawCat getWithdrawById(int withdrawCat);

	public boolean save(WithdrawCat cat);

}
