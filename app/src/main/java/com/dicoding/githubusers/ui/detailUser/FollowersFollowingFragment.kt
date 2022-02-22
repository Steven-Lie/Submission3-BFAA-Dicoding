package com.dicoding.githubusers.ui.detailUser

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubusers.adapter.UserAdapter
import com.dicoding.githubusers.api.UsersData
import com.dicoding.githubusers.databinding.FragmentFollowersFollowingBinding

class FollowersFollowingFragment : Fragment() {
    private val viewModel by activityViewModels<DetailUserViewModel>()
    private var _fragmentFollowersFollowing: FragmentFollowersFollowingBinding? = null
    private val binding get() = _fragmentFollowersFollowing as FragmentFollowersFollowingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _fragmentFollowersFollowing = FragmentFollowersFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val index = arguments?.getInt(ARG_SECTION_NUMBER, 0)
        val username = arguments?.getString(USERNAME)

        if (index == 0){
            viewModel.getFollowersList(username.toString(), activity)
            viewModel.followers.observe(viewLifecycleOwner, { users->
                showFollow(users)
            })
        } else {
            viewModel.getFollowingList(username.toString(), activity)
            viewModel.following.observe(viewLifecycleOwner, { users->
                showFollow(users)
            })
        }
    }

    private fun showFollow(users: List<UsersData>){
        if (users.isEmpty()){
            binding.tvNoResultFollow.visibility = View.VISIBLE
        } else {
            binding.tvNoResultFollow.visibility = View.GONE
        }
        binding.rvFollowersFollowing.layoutManager = LinearLayoutManager(activity)
        val userAdapter = UserAdapter(users)
        binding.rvFollowersFollowing.adapter = userAdapter
        userAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UsersData) {
                showSelectedUser(data)
            }
        })
    }

    private fun showSelectedUser(user: UsersData){
        val detailUserIntent = Intent(activity, DetailUserActivity::class.java)
        detailUserIntent.putExtra(DetailUserActivity.EXTRA_USERNAME, user.login)
        startActivity(detailUserIntent)
    }

    companion object{
        private const val ARG_SECTION_NUMBER = "section_number"
        private const val USERNAME = "username"

        @JvmStatic
        fun newInstance(index: Int, username: String) =
            FollowersFollowingFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, index)
                    putString(USERNAME, username)
                }
            }
    }
}