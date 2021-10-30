package com.accenture.pay.gateway.service

import com.accenture.pay.gateway.entity.Account
import com.accenture.pay.gateway.exception.NotFoundException
import com.accenture.pay.gateway.exception.UnprocessableStateException
import com.accenture.pay.gateway.model.Status
import com.accenture.pay.gateway.repository.AccountRepository
import org.slf4j.LoggerFactory
import org.springframework.integration.support.locks.LockRegistry
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AccountServiceImpl(private val accountRepository: AccountRepository, private val lockRegistry: LockRegistry) :
    AccountService {
    val LOGGER = LoggerFactory.getLogger(AccountServiceImpl::class.java)

    override fun getAccount(id: Int): Account {
        return accountRepository.findById(id).orElseThrow {
            NotFoundException(
                "Account with id = $id not found"
            )
        }
    }

    /**
     * From author - for sure this locks, repository and declarative transactions
     * here is a point for long discussion, as well as design in
     * compare to regular debit / credit / transactions logs
     *
     * @param from account id to transfer from
     * @param to account id to transfer to
     * @param amount amount to transfer
     * @param runnable is used for test purposes
     * @return status of fund transfer
     */
    @Transactional
    override fun transfer(from: Int, to: Int, amount: Double, runnable: Runnable?): Status {
        runnable?.run()
        val accountTransferFrom = getAccount(from)
        val accountTransferTo = getAccount(to)
        val lockFromAccount = lockRegistry.obtain(accountTransferFrom.id)
        lockFromAccount.lock()
        val lockToAccount = lockRegistry.obtain(accountTransferTo.id)
        lockToAccount.lock()
        return try {
            val transferFromBalance = accountTransferFrom.balance
            val transferToBalance = accountTransferTo.balance
            if (transferFromBalance!! < amount) return Status.FAILED
            accountTransferFrom.balance = transferFromBalance - amount
            accountTransferTo.balance = transferToBalance!! + amount
            accountRepository.save(accountTransferFrom)
            accountRepository.save(accountTransferTo)
            Status.SUCCESS
        } catch (ex: Exception) {
            LOGGER.warn("Unable to make a fund transfer due exception", ex)
            throw UnprocessableStateException("Unable to make a fund transfer")
        } finally {
            lockFromAccount.unlock()
            lockToAccount.unlock()
        }
    }

}