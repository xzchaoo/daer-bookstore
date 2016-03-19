package org.xzc.sshb.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
public class TestDomain {
	@Resource
	private SessionFactory sessionFactory;

	private Session s;
	private static Random r = new Random();

	@Before
	public void before() {
		s = sessionFactory.getCurrentSession();
	}

	@Test
	public void testAll() {
		User u1 = createUser();
		User u2 = createUser();
		Item i1 = createItem();
		Item i2 = createItem();
		Item i3 = createItem();

		Record r = new Record();
		r.setUser( u1 );
		Set<RecordItem> ris = new HashSet<RecordItem>();
		ris.add( new RecordItem( r, i1, 1 ) );
		ris.add( new RecordItem( r, i2, 2 ) );
		ris.add( new RecordItem( r, i3, 3 ) );
		r.setRecordItems( ris );

		s.save( u1 );
		s.save( i1 );
		s.save( i2 );
		s.save( i3 );

		s.save( r );
	}

	@Test
	public void testAll2() {
		User u = (User) s.get( User.class, 8 );
		Set<Record> set = u.getRecords();
		for (Record r : set) {
			System.out.println( "record" + r.getId() );
			for (RecordItem ri : r.getRecordItems()) {
				System.out.println( ri.getItem().getName() + "*" + ri.getQuantity() );
			}
			System.out.println( r.getTotalPrice() );
		}
	}

	@Test
	public void testUser() {// 增删改查
		User u = createUser();
		s.save( u );

		s.clear();

		u.setName( u.getName() + "3" );

		s.update( u );
		s.flush();
		s.clear();

		User u2 = (User) s.get( User.class, u.getId() );
		assertTrue( u != u2 );
		assertEquals( u.getId(), u2.getId() );
		assertEquals( u.getName(), u2.getName() );
		assertEquals( u.getAge(), u2.getAge() );
		assertEquals( u.getAddress(), u2.getAddress() );
		assertEquals( u.getSex(), u2.getSex() );
		assertEquals( u.getEmail(), u2.getEmail() );
		assertEquals( u.getPassword(), u2.getPassword() );

		s.delete( u2 );

		assertTrue( !s.contains( u ) );
	}

	private static Item createItem() {
		Item i = new Item();
		i.setName( UUID.randomUUID().toString() );
		i.setPrice( r.nextDouble() * 100 );
		i.setRemain( r.nextInt( 100 ) );
		i.setShortDescription( UUID.randomUUID().toString() );
		return i;
	}

	private static User createUser() {
		User u = new User();
		u.setName( UUID.randomUUID().toString() );
		u.setAge( r.nextInt( 100 ) );
		u.setAddress( "上海交通大学" );
		u.setEmail( "70862045@qq.com" );
		u.setMoney( r.nextDouble() * 1000 );
		u.setPassword( "70862045" );
		u.setSex( r.nextInt( 3 ) );
		return u;
	}
}
